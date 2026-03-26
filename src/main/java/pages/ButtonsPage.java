package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class ButtonsPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = ".btn.btn-primary:not(.btn-sm):not(.btn-md):not(.btn-lg):not([disabled])")
    public WebElement primaryButton;

    @FindBy(css = ".btn.btn-secondary")
    public WebElement secondaryButton;

    @FindBy(css = ".btn.btn-danger")
    public WebElement dangerButton;

    @FindBy(css = ".btn.btn-ghost")
    public WebElement ghostButton;

    @FindBy(css = ".btn.btn-outline")
    public WebElement outlineButton;

    @FindBy(css = ".btn.btn-sm")
    public WebElement smallButton;

    @FindBy(css = ".btn.btn-md")
    public WebElement mediumButton;

    @FindBy(css = ".btn.btn-lg")
    public WebElement largeButton;

    @FindBy(css = "button[disabled]:not(.loading)")
    public WebElement disabledButton;

    @FindBy(css = "main .btn")
    public List<WebElement> allButtons;

    @FindBy(css = ".result-text")
    public WebElement resultText;
}
