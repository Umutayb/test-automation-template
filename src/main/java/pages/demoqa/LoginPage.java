package pages.demoqa;

import common.PageObject;
import org.openqa.selenium.support.FindBy;
import pages.demoqa.components.LoginWrapper;


public class LoginPage extends PageObject {
    @FindBy(css = ".login-wrapper")
    LoginWrapper loginWrapper;
}
