package ru.msvdev.desktop.utils.scene;

import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Getter
@Component
public class PrimaryStage {

    private Stage stage;

    public void setStage(Stage primaryStage) {
        if (this.stage != null) {
            throw new RuntimeException("Нельзя переопределять Stage, созданный JavaFx в момент старта приложения");
        }
        this.stage = primaryStage;
    }
}
