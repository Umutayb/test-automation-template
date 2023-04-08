package pages.demoqa.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.utilities.WebUtilities;

import java.util.List;

public class PracticeForm extends WebUtilities {

    @FindBy(css = "h5")
    public WebElement header;

    @FindBy(id = "firstName")
    public WebElement firstNameInput;

    @FindBy(id = "lastName")
    public WebElement lastNameInput;

    @FindBy(id = "userEmail")
    public WebElement userEmailInput;

    @FindBy(id = "userNumber")
    public WebElement userNumber;

    @FindBy(id = "dateOfBirthInput")
    public WebElement dateOfBirthInput;

    @FindBy(id = "subjectsInput")
    public WebElement subjectsInput;

    @FindBy(css = "[for*='gender-radio']")
    public List<WebElement> genderRadioButtons;

    @FindBy(css = "[for*='hobbies-checkbox']")
    public List<WebElement> hobbiesCheckboxes;

    @FindBy(id = "uploadPicture")
    public WebElement uploadPictureButton;

    @FindBy(id = "currentAddress")
    public WebElement currentAddressInput;

    @FindBy(id = "state")
    public WebElement stateDropdown;

    @FindBy(id = "city")
    public WebElement cityDropdown;

    @FindBy(css = "[id*='react-select']")
    public List<WebElement> dropdownOptions;

    @FindBy(id = "submit")
    public WebElement submitButton;

}
