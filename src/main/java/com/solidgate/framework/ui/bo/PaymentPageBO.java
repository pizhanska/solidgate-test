package com.solidgate.framework.ui.bo;

import com.solidgate.framework.ui.pages.PaymentPage;

public class PaymentPageBO {
    private PaymentPage paymentPage = new PaymentPage();
    private String paymentStatusUrlPart = "example/";

    public void processPayment(String cardNumber, String expDate, String ccv, String cardHolder) {
        paymentPage.waitForPageLoad();
        paymentPage.setCardNumb(cardNumber);
        paymentPage.setExpDate(expDate);
        paymentPage.setCCVCard(ccv);
        paymentPage.setCardHolderName(cardHolder);
        paymentPage.clickSubmit();
    }

    public String getPaymentStatus() {
        paymentPage.waitForPageLoad();
        paymentPage.waitForUrlContains(10, paymentStatusUrlPart);
        return paymentPage.getUrl().split(paymentStatusUrlPart)[1];
    }
}
