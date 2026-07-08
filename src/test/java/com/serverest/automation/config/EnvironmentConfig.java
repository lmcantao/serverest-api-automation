package com.serverest.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = EnvironmentConfig.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EnvironmentConfig() {
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout"));
    }
}