package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class LoginFormPage {

    @FindBy(css = "h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='login-username']")
    public WebElement usernameInput;

    @FindBy(css = "[data-testid='login-password']")
    public WebElement passwordInput;

    @FindBy(css = "[data-testid='login-submit']")
    public WebElement signInButton;

    @FindBy(css = "[data-testid='login-show-password']")
    public WebElement showPasswordButton;

    @FindBy(css = ".hint")
    public WebElement hintText;
}
