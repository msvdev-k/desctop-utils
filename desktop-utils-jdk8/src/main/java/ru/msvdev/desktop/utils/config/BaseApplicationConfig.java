package ru.msvdev.desktop.utils.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.msvdev.desktop.utils.DesktopApplication;


@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackageClasses = {DesktopApplication.class})
public class BaseApplicationConfig {
}
