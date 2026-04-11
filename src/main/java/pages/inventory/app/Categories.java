package pages.inventory.app;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Categories {

    @FindBy(xpath = "//*[@AutomationId='textBox2']")
    WebElement categoryInput;
}
