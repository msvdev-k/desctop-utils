package ru.msvdev.desktop.utils.scene;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает сцену выводимую при старте приложения.
 * Аннотацией можно пометить только один класс сцены
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartApplicationScene {
}
