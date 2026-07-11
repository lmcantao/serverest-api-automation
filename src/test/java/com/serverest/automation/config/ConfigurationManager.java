package com.serverest.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigurationManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigurationManager.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new IllegalStateException("application.properties not found.");
            }

            PROPERTIES.load(input);

        } catch (IOException exception) {
            throw new RuntimeException("Error loading application.properties", exception);
        }
    }

    private ConfigurationManager() {
    }

    public static String getBaseUrl() {
        return PROPERTIES.getProperty("api.base.url");
    }

    public static Integer getTimeout() {
        return Integer.parseInt(PROPERTIES.getProperty("api.timeout"));
    }

}