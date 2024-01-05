package ru.msvdev.desktop.utils.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.sqlite.hibernate.dialect.SQLiteDialect;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class DataFileProvider implements FileManager, PlatformTransactionManager {

    private static final String DB_URL_TEMPLATE = "jdbc:sqlite:%s?DATE_CLASS=text";

    private Set<Class<?>> entityAnnotatedClasses;

    private final List<RepositoryFactoryUpdater> repositoryFactoryUpdaters;

    private HikariDataSource dataSource;
    private EntityManagerFactory entityManagerFactory;
    private JpaTransactionManager transactionManager;
    private EntityManager entityManager;


    @Override
    public void openFile(Path path) {
        closeFile();
        connectToDB(path.toString());
        migrate();
        vacuum();
        configureDataAccess();
    }

    @Override
    public void closeFile() {

        if (entityManager != null) {
            entityManager.close();
            entityManager = null;
        }

        transactionManager = null;

        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }

        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }


    /**
     * Установить соединение с файлом БД
     *
     * @param db путь к файлу БД
     */
    private void connectToDB(String db) {
        String url = String.format(DB_URL_TEMPLATE, db);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Актуализировать версию БД
     */
    private void migrate() {
        Flyway
                .configure()
                .dataSource(dataSource)
                .load()
                .migrate();
    }

    /**
     * Перестроить файл БД
     */
    private void vacuum() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("VACUUM");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сконфигурировать EntityManagerFactory, PlatformTransactionManager
     * и RepositoryFactorySupport
     */
    private void configureDataAccess() {
        // == Hibernate ==========
        Configuration configuration = new Configuration();
        entityAnnotatedClasses.forEach(configuration::addAnnotatedClass);

        StandardServiceRegistry standardServiceRegistry =
                new StandardServiceRegistryBuilder(new BootstrapServiceRegistryBuilder().build())
                        .applySetting(Environment.DIALECT, SQLiteDialect.class)
                        .applySetting(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
                        .applySetting(Environment.DATASOURCE, dataSource)
                        .applySetting(Environment.GLOBALLY_QUOTED_IDENTIFIERS, true)
                        .applySetting(Environment.HBM2DDL_AUTO, "validate")
                        .applySetting(Environment.SHOW_SQL, false)
//                        .applySetting(Environment.FORMAT_SQL, true)
//                        .applySetting(Environment.HIGHLIGHT_SQL, true)
                        .build();

        entityManagerFactory = configuration.buildSessionFactory(standardServiceRegistry);


        // == JpaTransactionManager ==========
        transactionManager = new JpaTransactionManager(entityManagerFactory);


        // == JpaRepositoryFactory ==========
        entityManager = SharedEntityManagerCreator
                .createSharedEntityManager(entityManagerFactory);

        JpaRepositoryFactory repositoryFactory = new JpaRepositoryFactory(entityManager);
        repositoryFactoryUpdaters.forEach(
                updater -> updater.repositoryFactoryUpdate(repositoryFactory)
        );
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        return transactionManager.getTransaction(definition);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        transactionManager.commit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        transactionManager.rollback(status);
    }
}
