package pages.demoqa.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.utilities.WebUtilities;

public class LoginWrapper extends WebUtilities {

    @FindBy(id = "userName")
    public WebElement userName;

    @FindBy(id = "password")
    public WebElement password;

    @FindBy(id = "login")
    public WebElement loginButton;

}
