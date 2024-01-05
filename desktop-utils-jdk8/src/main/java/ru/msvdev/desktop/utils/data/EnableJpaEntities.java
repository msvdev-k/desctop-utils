package ru.msvdev.desktop.utils.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableJpaEntities {

    /**
     * Список базовых пакетов для поиска классов сущностей JPA.
     * Пример: {@code @EnableJpaEntities("org.my.pkg")}.
     */
    String[] value() default {};

}
