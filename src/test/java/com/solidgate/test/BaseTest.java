package com.solidgate.test;

import com.solidgate.framework.api.core.HttpClientOperations;
import com.solidgate.framework.helpers.PropertiesReader;
import com.solidgate.framework.ui.bo.PaymentPageBO;
import com.solidgate.framework.ui.utils.DriverManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class BaseTest {
    private final String PROPS_FILE = "base";
    PaymentPageBO paymentPageBO;
    HttpClientOperations httpClientOperations;
    private PropertiesReader propertiesReader;
    private String publicKey;
    private String secretKey;
    private String paymentUrl;
    private String cardUrl;

    @BeforeClass(alwaysRun = true)
    public void readProps() {
        propertiesReader = new PropertiesReader(PROPS_FILE);
        publicKey = System.getProperty("publicKey");
        secretKey = System.getProperty("secretKey");
        paymentUrl = propertiesReader.getProperty("payment_page_url");
        cardUrl = propertiesReader.getProperty("card_payments_url");
        httpClientOperations = new HttpClientOperations(getPaymentUrl());
        paymentPageBO = new PaymentPageBO();
        DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    @AfterClass
    public void driverQuit() {
        DriverManager.quit();
    }
}
