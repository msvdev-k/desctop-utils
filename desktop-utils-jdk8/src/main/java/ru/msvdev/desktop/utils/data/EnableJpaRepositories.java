package ru.msvdev.desktop.utils.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableJpaRepositories {

    /**
     * Список базовых пакетов для поиска интерфейсов репозиториев JPA.
     * Пример: {@code @EnableJpaRepositories("org.my.pkg")}.
     */
    String[] value() default {};

}
