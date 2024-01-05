package ru.msvdev.desktop.utils.data;

import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public interface RepositoryFactoryUpdater {

    /**
     * Обновление сведений о репозиториях
     *
     * @param repositoryFactorySupport фабрика для создания репозиториев на основе интерфейсов
     */
    void repositoryFactoryUpdate(RepositoryFactorySupport repositoryFactorySupport);
}
