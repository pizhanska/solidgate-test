package com.solidgate.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.solidgate.framework.api.core.HttpClientOperations;
import com.solidgate.framework.api.model.response.PaymentPageResponseModel;
import com.solidgate.framework.helpers.DataPrepHelper;
import com.solidgate.framework.helpers.JsonOperationsHelper;
import com.solidgate.framework.helpers.SignatureGenerator;
import com.solidgate.framework.ui.utils.DriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.awaitility.Awaitility;
import org.openqa.selenium.remote.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class PaymentPageTest extends BaseTest {
    private final String CARD_NUMB = "4067429974719265";
    private final String EXP_DATE = "429";
    private JsonOperationsHelper jsonOperationsHelper = new JsonOperationsHelper();
    private String testData;
    private String signaturePaymentPage;
    private String signatureOrderStatus;
    private JsonNode jsonNodeOrderRequest;
    private JsonNode jsonNodeOderResponse;
    private String orderIdJsonString;


    @BeforeMethod(alwaysRun = true)
    public void generateSignature() {
        testData = DataPrepHelper.getPaymentPageDataWithUniqueOrderId("payment-data");
        signaturePaymentPage = SignatureGenerator.generateSignature(getPublicKey(), String.valueOf(testData), getSecretKey());
        jsonNodeOrderRequest = DataPrepHelper.getOrderNode(testData);
        orderIdJsonString = DataPrepHelper.getOrderIdJsonString(testData);
        signatureOrderStatus = SignatureGenerator.generateSignature(getPublicKey(), String.valueOf(orderIdJsonString), getSecretKey());

    }

    @Test(description = "Create Payment Page through API and then process Payment though UI. Verify that payment is successful.")
    public void testPaymentPageCreationAndPaymentProcessing() {
        HttpResponse response = httpClientOperations.postOperation(getPaymentUrl(), "init", getPublicKey(), signaturePaymentPage, DataPrepHelper.createContentSupplier(testData));
        Assert.assertEquals(200, response.getStatus());
        PaymentPageResponseModel paymentPageResponseModel = jsonOperationsHelper.paymentPageResponseJsonToObject(response.toString());
        Assert.assertTrue(paymentPageResponseModel.getURL().contains("https://payment-page.solidgate.com"));
        DriverManager.getDriver().navigate().to(paymentPageResponseModel.getURL());
        paymentPageBO.processPayment(CARD_NUMB, EXP_DATE, RandomStringUtils.randomNumeric(3), RandomStringUtils.randomAlphabetic(8));
        Assert.assertEquals(paymentPageBO.getPaymentStatus(), "success");

        // UI is not showing message that payment is successful, instead it redirects to page that contains in URL "success" and show page is not loaded.
        // So the last assert is alternative
    }

    @Test(description = "Check order status after payment processing and verify amount and currency of returned order details")
    public void testOrderStatusAfterPaymentProcessing() {
        // Create Payment Page and process payment

        createPaymentAndProcess();

        //wait for "auth_ok" status - successful reservation of funds for the transaction.

        Awaitility
                .await()
                .atMost(20, TimeUnit.SECONDS)
                .with()
                .pollInterval(3, TimeUnit.SECONDS)
                .untilAsserted(() -> Assert.assertTrue(httpClientOperations.postOperation(getCardUrl(),
                        "status", getPublicKey(), signatureOrderStatus,
                        DataPrepHelper.createContentSupplier(orderIdJsonString)).toString().contains("auth_ok")));

        //Check status
        httpClientOperations = new HttpClientOperations(getCardUrl());
        HttpResponse response = httpClientOperations.postOperation(getCardUrl(), "status", getPublicKey(), signatureOrderStatus, DataPrepHelper.createContentSupplier(orderIdJsonString));
        jsonNodeOderResponse = DataPrepHelper.getOrderNodeFromOrderStatusResponse(response);
        Assert.assertEquals(jsonNodeOderResponse.get("status").toString(), "\"auth_ok\"");
        Assert.assertEquals(jsonNodeOderResponse.get("amount"), jsonNodeOrderRequest.get("amount"));
        Assert.assertEquals(jsonNodeOderResponse.get("currency"), jsonNodeOrderRequest.get("currency"));

    }

    private void createPaymentAndProcess() {
        HttpResponse response = httpClientOperations.postOperation(getPaymentUrl(), "init", getPublicKey(), signaturePaymentPage, DataPrepHelper.createContentSupplier(testData));
        PaymentPageResponseModel paymentPageResponseModel = jsonOperationsHelper.paymentPageResponseJsonToObject(response.toString());
        DriverManager.getDriver().navigate().to(paymentPageResponseModel.getURL());
        paymentPageBO.processPayment(CARD_NUMB, EXP_DATE, RandomStringUtils.randomNumeric(3), RandomStringUtils.randomAlphabetic(8));
    }
}
