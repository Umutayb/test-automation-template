package pages;

import common.PageObject;
import org.openqa.selenium.support.FindBy;
import pages.components.LoginWrapper;


public class LoginPage extends PageObject {
    @FindBy(css = ".login-wrapper")
    LoginWrapper loginWrapper;
}
