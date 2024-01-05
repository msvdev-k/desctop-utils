package ru.msvdev.desktop.utils.task;

/**
 * Базовый класс для всех задач чтения/записи данных.
 * Все классы задач данного топа должны быть унаследованы от этого базового класса
 *
 * @param <T> тип результата, возвращаемого в случае успешного завершения задачи
 */
public abstract class DataTaskBase<T> extends TaskBase<T> {
    public DataTaskBase() {
        super();
    }
}
