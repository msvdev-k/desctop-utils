package ru.msvdev.desktop.utils.event.dispatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.msvdev.desktop.utils.event.emitter.DataAccessEventEmitter;
import ru.msvdev.desktop.utils.event.listener.DataAccessEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataAccessEventDispatcher implements DataAccessEventEmitter {

    private final List<DataAccessEventListener> dataAccessEventListeners;

    @Override
    public void fileOpenEmin() {
        dataAccessEventListeners.forEach(DataAccessEventListener::fileOpenEvent);
    }

    @Override
    public void fileCloseEmit() {
        dataAccessEventListeners.forEach(DataAccessEventListener::fileCloseEvent);
    }

}
