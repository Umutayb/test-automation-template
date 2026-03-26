package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class FormsPage extends PickleibPageObject {

    @FindBy(id = "title")
    public WebElement pageTitle;

    @FindBy(id = "name")
    public WebElement nameInput;

    @FindBy(id = "email")
    public WebElement emailInput;

    @FindBy(id = "gender")
    public WebElement genderSelect;

    @FindBy(id = "mobile")
    public WebElement mobileInput;

    @FindBy(id = "hobbies")
    public WebElement hobbiesInput;

    @FindBy(id = "currentAddress")
    public WebElement addressInput;

    @FindBy(id = "city")
    public WebElement cityInput;

    @FindBy(id = "submit")
    public WebElement submitButton;

    @FindBy(id = "submission-title")
    public WebElement submissionTitle;

    @FindBy(css = ".submitted-info-table tr")
    public List<WebElement> submittedRows;

    @FindBy(css = ".submitted-info-table .table-value")
    public List<WebElement> submittedValues;

    @FindBy(css = ".submitted-info-table .table-key")
    public List<WebElement> submittedKeys;

    @FindBy(css = ".modal .close")
    public WebElement modalCloseButton;

    @FindBy(css = ".error-message")
    public WebElement errorMessage;
}
