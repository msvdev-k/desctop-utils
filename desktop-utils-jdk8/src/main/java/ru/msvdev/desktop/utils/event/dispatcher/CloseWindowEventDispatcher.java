package ru.msvdev.desktop.utils.event.dispatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.msvdev.desktop.utils.event.emitter.CloseWindowEventEmitter;
import ru.msvdev.desktop.utils.event.listener.CloseWindowEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CloseWindowEventDispatcher implements CloseWindowEventEmitter {

    private final List<CloseWindowEventListener> closeWindowEventListeners;

    @Override
    public void closeWindowEvent() {
        closeWindowEventListeners.forEach(CloseWindowEventListener::closeWindowEvent);
    }

}
