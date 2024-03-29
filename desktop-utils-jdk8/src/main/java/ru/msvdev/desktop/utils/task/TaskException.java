package ru.msvdev.desktop.utils.task;

import lombok.Getter;

/**
 * Класс, описывающий все исключения возникающие при выполнении задач
 */
@Getter
public class TaskException extends RuntimeException {

    private final Type type;

    public TaskException(Throwable cause) {
        this("", cause);
    }

    public TaskException(String message, Throwable cause) {
        this(Type.ERROR, message, cause);
    }

    public TaskException(Type type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }


    public enum Type {
        /**
         * Информационное исключение (не критичное)
         */
        INFORMATION,

        /**
         * Предупреждающее исключение (не критичное)
         */
        WARNING,

        /**
         * Критическая ошибка
         */
        ERROR
    }
}
