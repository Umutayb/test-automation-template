package pages.inventory.app;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.mobile.PickleibScreenObject;

public class LoginFrame extends PickleibScreenObject {

    @FindBy(xpath = "//*[@Name='Username Input']")
    WebElement loginInput;

    @FindBy(xpath = "//*[@Name='Password Input']")
    WebElement passwordInput;

    @FindBy(xpath = "//*[@Name='Submit Button']")
    WebElement submitButton;

    @FindBy(xpath = "//*[@Name='REGISTER']")
    WebElement registerButton;

    @FindBy(xpath = "//*[@Name='LOGIN']")
    WebElement loginButton;

    @FindBy(xpath = "//*[@Name='OK']")
    WebElement OKButton;

}
