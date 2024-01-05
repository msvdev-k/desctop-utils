package ru.msvdev.desktop.utils.scene;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class SceneSwitcher {

    private final PrimaryStage primaryStage;
    private final List<ApplicationScene> scenes;

    private Class<? extends ApplicationScene> currentSceneClass;
    private Map<Class<? extends ApplicationScene>, ApplicationScene> applicationScenes;

    private Class<? extends ApplicationScene> startScene;


    @PostConstruct
    private void init() {
        applicationScenes = new HashMap<>();

        for (ApplicationScene scene : scenes) {
            Class<? extends ApplicationScene> sceneClass = scene.getClass();
            applicationScenes.put(sceneClass, scene);

            if (sceneClass.isAnnotationPresent(StartApplicationScene.class)) {
                if (startScene != null) {
                    throw new RuntimeException("Только один из классов ApplicationScene может быть помечен аннотацией @StartApplicationScene");
                }
                startScene = sceneClass;
            }
        }

        if (startScene == null) {
            throw new RuntimeException("Необходимо один из классов ApplicationScene пометить аннотацией @StartApplicationScene");
        }
    }


    public <T extends ApplicationScene> void switchSceneTo(Class<T> newSceneClass) {
        ApplicationScene newScene = applicationScenes.get(newSceneClass);
        try {
            newScene.switchScene(primaryStage.getStage(), currentSceneClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        currentSceneClass = newSceneClass;
    }

    /**
     * Показать первую начальную сцену приложения.
     * Данный метод вызывается только один раз при старте приложения,
     * поэтому не стоит вызывать этот метод самостоятельно
     */
    public void show() {
        if (startScene == null) {
            throw new RuntimeException("Ошибка при отображении первой сцены приложения");
        }
        switchSceneTo(startScene);
        startScene = null;
    }
}
