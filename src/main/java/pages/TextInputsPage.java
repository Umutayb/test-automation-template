package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

@PageObject
public class TextInputsPage extends PickleibPageObject {

    @FindBy(css = "h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='input-text']")
    public WebElement textInput;

    @FindBy(css = "[data-testid='input-password']")
    public WebElement passwordInput;

    @FindBy(css = "[data-testid='input-email']")
    public WebElement emailInput;

    @FindBy(css = "[data-testid='input-number']")
    public WebElement numberInput;

    @FindBy(css = "[data-testid='input-textarea']")
    public WebElement textareaInput;

    @FindBy(css = "[data-testid='input-disabled']")
    public WebElement disabledInput;

    @FindBy(css = "[data-testid='input-error']")
    public WebElement errorInput;

    @FindBy(css = "[data-testid='input-success']")
    public WebElement successInput;

    @FindBy(css = "[data-testid='input-values']")
    public WebElement currentValues;
}
