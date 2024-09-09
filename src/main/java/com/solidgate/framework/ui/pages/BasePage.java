package com.solidgate.framework.ui.pages;

import com.solidgate.framework.ui.utils.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class BasePage {

    protected static final Logger log = Logger.getLogger(BasePage.class.getName());
    private static final int DEFAULT_TIMEOUT = 120;
    private static final int PAGE_LOAD_TIMEOUT = 50;
    private static final int POLLING_INTERVAL = 2;

    protected Wait<WebDriver> wait;

    public BasePage() {
        WebDriver driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
        this.wait = createFluentWait(driver, DEFAULT_TIMEOUT);
    }

    public void waitForPageLoad() {
        WebDriver driver = DriverManager.getDriver();
        new WebDriverWait(driver, Duration.ofSeconds(PAGE_LOAD_TIMEOUT))
                .until((ExpectedCondition<Boolean>) wd ->
                        ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    public boolean waitForControl(WebElement webControl) {
        log.info("Waiting for control of element");
        return wait.until(driver -> webControl != null && webControl.isEnabled() && webControl.isDisplayed());
    }

    private FluentWait<WebDriver> createFluentWait(WebDriver driver, int timeoutInSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofSeconds(POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class);
    }

    public String getUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    public void waitForUrlContains(int seconds, String urlContains) {
        WebDriver driver = DriverManager.getDriver();
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.urlContains(urlContains));
    }
}
