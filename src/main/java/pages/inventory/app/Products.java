package pages.inventory.app;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.platform.PickleibScreenObject;

import java.util.List;

public class Products extends PickleibScreenObject {

    @FindBy(xpath = "//*[@AutomationId='textBox1']")
    WebElement nameInput;

    @FindBy(xpath = "//*[@AutomationId='textBox2']")
    WebElement priceInput;

    @FindBy(xpath = "//*[@AutomationId='textBox3']")
    WebElement stockInput;

    @FindBy(xpath = "//*[@AutomationId='textBox4']")
    WebElement unitInput;

    @FindBy(xpath = "//*[@Name='Open']")
    WebElement categoryDropdown;

    @FindBy(xpath = "//*[@Name='Groceries']")
    WebElement groceriesSelection;

    @FindBy(xpath = "//*[@Name='Unit']")
    WebElement groceriesInput;

    @FindBy(xpath = "//*[@Name=' Row 0']")
    WebElement addToCart;

    @FindBy(xpath = "//*[@LocalizedControlType='list item']")
    List<WebElement> categoryList;
}
