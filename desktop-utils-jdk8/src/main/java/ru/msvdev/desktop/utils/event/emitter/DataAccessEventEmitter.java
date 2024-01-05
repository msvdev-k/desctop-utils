package ru.msvdev.desktop.utils.event.emitter;


public interface DataAccessEventEmitter {

    /**
     * Имитировать событие открытия файла и доступа к данным
     */
    void fileOpenEmin();

    /**
     * Имитировать событие закрытия файла
     */
    void fileCloseEmit();
}
