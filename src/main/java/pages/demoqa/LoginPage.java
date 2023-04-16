package pages.demoqa;

import org.openqa.selenium.support.FindBy;
import pages.demoqa.components.LoginWrapper;
import pickleib.utilities.WebUtilities;


public class LoginPage extends WebUtilities {
    @FindBy(css = ".login-wrapper")
    LoginWrapper loginWrapper;
}
