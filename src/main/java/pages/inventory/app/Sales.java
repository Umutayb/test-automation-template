package pages.inventory.app;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.platform.PickleibScreenObject;

public class Sales extends PickleibScreenObject {
    @FindBy(xpath = "//*[@Name='CHECK OUT']")
    WebElement checkOutButton;

    @FindBy(xpath = "//*[@AutomationId='textBox2']")
    WebElement paymentAmountInput;

    @FindBy(xpath = "//*[@Name='PAY']")
    WebElement paymentButton;

}
