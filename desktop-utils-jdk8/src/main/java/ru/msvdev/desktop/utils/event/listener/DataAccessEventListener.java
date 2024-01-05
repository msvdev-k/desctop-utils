package ru.msvdev.desktop.utils.event.listener;


public interface DataAccessEventListener {

    /**
     * Файл и доступ к данным открыты
     */
    void fileOpenEvent();

    /**
     * Файл закрыт
     */
    void fileCloseEvent();
}
