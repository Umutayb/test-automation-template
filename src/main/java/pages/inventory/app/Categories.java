package pages.inventory.app;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.mobile.PickleibScreenObject;

public class Categories extends PickleibScreenObject {

    @FindBy(xpath = "//*[@AutomationId='textBox2']")
    WebElement categoryInput;
}
