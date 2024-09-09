package com.solidgate.framework.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private final Properties properties;

    public PropertiesReader(String fileName) {
        this.properties = loadProperties(fileName + ".properties");
    }

    private Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Properties file not found: " + fileName);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file: " + fileName, e);
        }
        return props;
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property not found: " + key);
        }
        return value;
    }

    public String getPropertyOrDefault(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
