package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class BookHiveSignupPage {

    @FindBy(css = "[data-testid='signup-username']")
    public WebElement usernameInput;

    @FindBy(css = "[data-testid='signup-email']")
    public WebElement emailInput;

    @FindBy(css = "[data-testid='signup-password']")
    public WebElement passwordInput;

    @FindBy(css = "[data-testid='signup-submit']")
    public WebElement createAccountButton;

    @FindBy(linkText = "Sign in")
    public WebElement signInLink;
}
