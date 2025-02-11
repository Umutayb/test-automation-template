package pages.winform;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.mobile.PickleibScreenObject;

public class LoginFrame extends PickleibScreenObject {

    @FindBy(name = "Password")
    WebElement loginInput;

    @FindBy(xpath = "//*[@AutomationId='txtPassword']")
    WebElement passwordInput;

}
