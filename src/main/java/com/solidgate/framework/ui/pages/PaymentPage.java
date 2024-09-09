package com.solidgate.framework.ui.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PaymentPage extends BasePage {

    @FindBy(id = "ccnumber")
    private WebElement cardNumb;

    @FindBy(xpath = "//input[@data-testid='cardExpiryDate']")
    private WebElement expDate;

    @FindBy(id = "cvv2")
    private WebElement ccv;

    @FindBy(xpath = "//input[@data-testid='cardHolder']")
    private WebElement cardHolder;

    @FindBy(xpath = "//button[@data-testid='submit']")
    private WebElement submitButton;

    public void setCardNumb(String cardNumber) {
        waitForControl(cardNumb);
        cardNumb.sendKeys(cardNumber);
    }

    public void setExpDate(String cardExpDate) {
        expDate.sendKeys(cardExpDate);
    }

    public void setCCVCard(String ccvCard) {
        ccv.sendKeys(ccvCard);
    }

    public void setCardHolderName(String cardHolderName) {
        waitForControl(cardNumb);
        //JavascriptExecutor js = (JavascriptExecutor)DriverManager.getDriver();
        //js.executeScript("arguments[0].value='Avinash Mishra';", cardHolder);
        //cardHolder.click();
        cardHolder.sendKeys(cardHolderName);
    }

    public void clickSubmit() {
        submitButton.click();
    }

}
