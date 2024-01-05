package ru.msvdev.desktop.utils.data;

import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;


public class JpaRepositoryProxy implements InvocationHandler, RepositoryFactoryUpdater {

    private Class<?> repositoryClass;
    private Set<String> repositoryMethodNames;
    private Object repository;


    @Override
    public void repositoryFactoryUpdate(RepositoryFactorySupport repositoryFactorySupport) {
        repository = repositoryFactorySupport.getRepository(repositoryClass);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("repositoryFactoryUpdate")) {
            return method.invoke(this, args);
        }

        if (repository != null && repositoryMethodNames.contains(method.getName())) {
            return method.invoke(repository, args);
        }

        return null;
    }
}
