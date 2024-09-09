package com.solidgate.framework.ui.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    private DriverManager() {
    }


    public static WebDriver getDriver() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        if (driver.get() == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            options.setExperimentalOption("prefs", prefs);
            driver.set(new ChromeDriver(options));
        }

        return driver.get();
    }


    public static void quit() {
        driver.get().quit();
        driver.remove();
    }
}
