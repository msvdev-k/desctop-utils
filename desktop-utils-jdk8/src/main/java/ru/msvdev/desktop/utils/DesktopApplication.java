package ru.msvdev.desktop.utils;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import ru.msvdev.desktop.utils.config.BaseApplicationConfig;
import ru.msvdev.desktop.utils.config.Constants;
import ru.msvdev.desktop.utils.data.DataFileProvider;
import ru.msvdev.desktop.utils.scene.PrimaryStage;
import ru.msvdev.desktop.utils.scene.SceneSwitcher;
import ru.msvdev.desktop.utils.task.TaskExecutor;

import java.util.concurrent.TimeUnit;


public class DesktopApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Constants.LOGGER_NAME);
    private static long startTime;

    private static ApplicationContext ctx;


    /**
     * Запуск приложения
     *
     * @param args             аргументы командной строки
     * @param componentClasses конфигурационные классы помеченные аннотацией {@link Configuration @Configuration}
     */
    public static void run(String[] args, Class<?>... componentClasses) {
        begin();
        initContext(componentClasses);
        launch(args);
        end();
    }


    /**
     * Начало работы приложения
     */
    public static void begin() {
        logger.info("Application start");
        startTime = System.nanoTime();
    }

    /**
     * Завершение работы приложения
     */
    public static void end() {
        long workingTime = System.nanoTime() - startTime;
        long days = TimeUnit.NANOSECONDS.toDays(workingTime);
        long hours = TimeUnit.NANOSECONDS.toHours(workingTime);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(workingTime);
        long seconds = TimeUnit.NANOSECONDS.toSeconds(workingTime);
        logger.info("Application stop (working {} days, {} hours, {} minutes and {} seconds)",
                days, hours - days * 24, minutes - hours * 60, seconds - minutes * 60);
    }

    /**
     * Инициализация контекста Spring
     *
     * @param componentClasses массив классов помеченных аннотацией {@link Configuration @Configuration}
     */
    private static void initContext(Class<?>... componentClasses) {
        Class<?>[] configClasses = new Class[componentClasses.length + 1];
        configClasses[0] = BaseApplicationConfig.class;
        System.arraycopy(componentClasses, 0, configClasses, 1, componentClasses.length);

        ctx = new AnnotationConfigApplicationContext(configClasses);
    }


    @Override
    public void start(Stage primaryStage) {
        ctx.getBean(PrimaryStage.class).setStage(primaryStage);

        SceneSwitcher sceneSwitcher = ctx.getBean(SceneSwitcher.class);
        sceneSwitcher.show();

        long startedTime = System.nanoTime();
        logger.info("Application started ({} s)", (startedTime - startTime) * 1e-9);
    }

    @Override
    public void stop() {
        TaskExecutor taskExecutor = ctx.getBean(TaskExecutor.class);
        taskExecutor.shutdownNow();

        DataFileProvider dataFileProvider = ctx.getBean(DataFileProvider.class);
        dataFileProvider.closeFile();
    }

}