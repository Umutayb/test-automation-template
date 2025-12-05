package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;


public class LoginPage extends PickleibPageObject {

    @FindBy(css = ".login-wrapper #userName")
    public WebElement userName;

    @FindBy(css = ".login-wrapper #password")
    public WebElement password;

    @FindBy(css = ".login-wrapper #login")
    public WebElement loginButton;

}
