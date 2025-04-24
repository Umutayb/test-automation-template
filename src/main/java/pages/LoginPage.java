package pages;

import common.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LoginPage extends PageObject {

    @FindBy(css = ".login-wrapper #userName")
    public WebElement userName;

    @FindBy(css = ".login-wrapper #password")
    public WebElement password;

    @FindBy(css = ".login-wrapper #login")
    public WebElement loginButton;

}
