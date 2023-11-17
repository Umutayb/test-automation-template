package pages.demoqa.components;

import com.github.webdriverextensions.WebComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginWrapper extends WebComponent {

    @FindBy(id = "userName")
    public WebElement userName;

    @FindBy(id = "password")
    public WebElement password;

    @FindBy(id = "login")
    public WebElement loginButton;

}
