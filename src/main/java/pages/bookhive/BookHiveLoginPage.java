package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

@PageObject
public class BookHiveLoginPage extends PickleibPageObject {

    @FindBy(css = "[data-testid='login-email']")
    public WebElement emailInput;

    @FindBy(css = "[data-testid='login-password']")
    public WebElement passwordInput;

    @FindBy(css = "[data-testid='login-submit']")
    public WebElement signInButton;

    @FindBy(css = "[data-testid='login-error']")
    public WebElement errorMessage;

    @FindBy(linkText = "Sign up")
    public WebElement signUpLink;
}
