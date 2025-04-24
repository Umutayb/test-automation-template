package pages;

import common.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class PracticeFormPage extends PageObject {

    @FindBy(css = ".practice-form-wrapper h5")
    public WebElement header;

    @FindBy(css = ".practice-form-wrapper #firstName")
    public WebElement firstNameInput;

    @FindBy(css = ".practice-form-wrapper #lastName")
    public WebElement lastNameInput;

    @FindBy(css = ".practice-form-wrapper #userEmail")
    public WebElement userEmailInput;

    @FindBy(css = ".practice-form-wrapper #userNumber")
    public WebElement userNumber;

    @FindBy(css = ".practice-form-wrapper #dateOfBirthInput")
    public WebElement dateOfBirthInput;

    @FindBy(css = ".practice-form-wrapper #subjectsInput")
    public WebElement subjectsInput;

    @FindBy(css = ".practice-form-wrapper [for*='gender-radio']")
    public List<WebElement> genderRadioButtons;

    @FindBy(css = ".practice-form-wrapper [for*='hobbies-checkbox']")
    public List<WebElement> hobbiesCheckboxes;

    @FindBy(css = ".practice-form-wrapper #uploadPicture")
    public WebElement uploadPictureButton;

    @FindBy(css = ".practice-form-wrapper #currentAddress")
    public WebElement currentAddressInput;

    @FindBy(css = ".practice-form-wrapper #state")
    public WebElement stateDropdown;

    @FindBy(css = ".practice-form-wrapper #city")
    public WebElement cityDropdown;

    @FindBy(css = ".practice-form-wrapper [class*='subjects-auto-complete__menu']")
    public List<WebElement> subjectOptions;

    @FindBy(css = ".practice-form-wrapper [id*='react-select']")
    public List<WebElement> dropdownOptions;

    @FindBy(css = ".practice-form-wrapper #submit")
    public WebElement submitButton;

    @FindBy(css = ".react-datepicker__month-container .react-datepicker__month-select option")
    List<WebElement> Months;

    @FindBy(css = ".react-datepicker__month-container .react-datepicker__year-select option")
    List<WebElement> Years;

    @FindBy(css = ".react-datepicker__month-container .react-datepicker__month [role='option']")
    List<WebElement> Days;

    @FindBy(css = ".modal-content #example-modal-sizes-title-lg")
    WebElement resultModelHeader;

    @FindBy(css = ".modal-content.modal-content tbody tr")
    List<WebElement> resultModelDataRows;

    @FindBy(css = ".modal-content #closeLargeModal")
    WebElement resultModelCloseButton;

}
