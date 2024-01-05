package ru.msvdev.desktop.utils.scene;

import javafx.stage.Stage;

public interface ApplicationScene {

    <T extends ApplicationScene> void switchScene(Stage primaryStage, Class<T> currentScene) throws Exception;
}
