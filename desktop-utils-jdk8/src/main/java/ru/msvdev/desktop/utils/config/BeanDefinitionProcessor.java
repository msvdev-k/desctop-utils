package ru.msvdev.desktop.utils.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.msvdev.desktop.utils.data.EnableJpaEntities;
import ru.msvdev.desktop.utils.data.EnableJpaRepositories;
import ru.msvdev.desktop.utils.data.JpaRepositoryProxy;
import ru.msvdev.desktop.utils.data.RepositoryFactoryUpdater;

import javax.persistence.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class BeanDefinitionProcessor implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

    /**
     * key - beanName
     * <p>
     * value - класс репозитория (интерфейс)
     */
    private final Map<String, Class<?>> repositoryBeanMap = new HashMap<>();

    /**
     * Список классов сущностей JPA
     */
    private final Set<Class<?>> entityClasses = new HashSet<>();


    /**
     * Поиск всех классов в указанном пакете
     *
     * @param packageName название пакета
     * @return список полных имён классов (с названием пакета)
     */
    private List<String> findAllClassNames(String packageName) throws IOException {
        try (InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"))) {

            assert stream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(className -> className.substring(0, className.length() - 6))
                    .map(className -> String.format("%s.%s", packageName, className))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Получить название бина по имени класса
     *
     * @param className название класса
     * @return название бина
     */
    private String getBeanNameFromClassName(String className) {
        String classCanonicalName = className.substring(className.lastIndexOf(".") + 1);
        return classCanonicalName.substring(0, 1).toLowerCase() + classCanonicalName.substring(1);
    }


    /**
     * Обновить список репозиториев
     *
     * @param beanClass класс конфигурационного бина
     */
    private void updateJpaRepositories(Class<?> beanClass) {
        EnableJpaRepositories enableJpaRepositories = beanClass.getAnnotation(EnableJpaRepositories.class);
        if (enableJpaRepositories == null) return;

        try {
            for (String packageName : enableJpaRepositories.value()) {
                List<String> classNames = findAllClassNames(packageName);

                for (String className : classNames) {
                    Class<?> classForName = Class.forName(className);

                    if (classForName.isAnnotationPresent(Repository.class) &&
                            classForName.isInterface()) {

                        String beanName = getBeanNameFromClassName(className);
                        repositoryBeanMap.put(beanName, classForName);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Обновить список классов сущностей JPA
     *
     * @param beanClass класс конфигурационного бина
     */
    private void updateJpaEntities(Class<?> beanClass) {
        EnableJpaEntities enableJpaEntities = beanClass.getAnnotation(EnableJpaEntities.class);
        if (enableJpaEntities == null) return;

        try {
            for (String packageName : enableJpaEntities.value()) {
                List<String> classNames = findAllClassNames(packageName);

                for (String className : classNames) {
                    Class<?> classForName = Class.forName(className);

                    if (classForName.isAnnotationPresent(Entity.class)) {
                        entityClasses.add(classForName);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Сканирование всех зарегистрированных бинов на предмет наличия
     * конфигурационных аннотаций
     */
    private void beanDefinitionRegistryScanner(BeanDefinitionRegistry registry) {
        try {
            String[] beanDefinitionNames = registry.getBeanDefinitionNames();

            for (String beanDefinitionName : beanDefinitionNames) {
                String beanClassName = registry
                        .getBeanDefinition(beanDefinitionName)
                        .getBeanClassName();

                if (beanClassName == null) continue;

                Class<?> beanClass = Class.forName(beanClassName);

                updateJpaRepositories(beanClass);
                updateJpaEntities(beanClass);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        beanDefinitionRegistryScanner(registry);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();

        // === Регистрация beans прокси репозиториев ===
        for (String beanName : repositoryBeanMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();

            beanDefinition.setBeanClassName(JpaRepositoryProxy.class.getName());
            beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (beanName.equals("dataFileProvider")) {
            Field[] fields = bean.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (field.getName().equals("entityAnnotatedClasses")) {
                        field.setAccessible(true);
                        field.set(bean, entityClasses);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if (repositoryBeanMap.containsKey(beanName)) {
            try {
                Class<?> repositoryClass = repositoryBeanMap.get(beanName);

                Field[] fields = bean.getClass().getDeclaredFields();

                for (Field field : fields) {
                    if (field.getName().equals("repositoryClass")) {
                        field.setAccessible(true);
                        field.set(bean, repositoryClass);
                        continue;
                    }

                    if (field.getName().equals("repositoryMethodNames")) {
                        Set<String> methodNames = Arrays.stream(repositoryClass.getMethods())
                                .map(Method::getName).collect(Collectors.toSet());

                        field.setAccessible(true);
                        field.set(bean, methodNames);
                    }
                }

                bean = Proxy.newProxyInstance(
                        ClassLoader.getSystemClassLoader(),
                        new Class[]{repositoryClass, RepositoryFactoryUpdater.class},
                        (InvocationHandler) bean);

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

}
