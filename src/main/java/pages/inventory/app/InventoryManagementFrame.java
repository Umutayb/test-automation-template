package pages.inventory.app;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.platform.PickleibScreenObject;

public class InventoryManagementFrame extends PickleibScreenObject {

    @FindBy(xpath = "//*[@AutomationId='radioButton2']")
    WebElement categoriesButton;

    @FindBy(xpath = "//*[@AutomationId='radioButton1']")
    WebElement productsButton;

    @FindBy(xpath = "//*[@AutomationId='radioButton5']")
    WebElement dashboardButton;

    @FindBy(xpath = "//*[@AutomationId='radioButton4']")
    WebElement transactionButton;

    @FindBy(xpath = "//*[@AutomationId='radioButton3']")
    WebElement salesButton;

    @FindBy(xpath = "//*[@AutomationId='button1']")
    WebElement logoutButton;

    @FindBy(xpath = "//*[@Name='OK']")
    WebElement OKButton;

    @FindBy(xpath = "//*[@AutomationId='button3']")
    WebElement deleteButton;

    @FindBy(xpath = "//*[@AutomationId='button2']")
    WebElement editButton;

    @FindBy(xpath = "//*[@Name='ADD']")
    WebElement addButton;

    @FindBy(xpath = "//*[@Name='SAVE']")
    WebElement saveButton;

    @FindBy(xpath = "//*[@Name='CANCEL']")
    WebElement cancelButton;
}
