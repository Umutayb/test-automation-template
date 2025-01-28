package uwp;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.mobile.PickleibScreenObject;

import java.util.List;

public class BasicUWPMainPage extends PickleibScreenObject {
    @FindBy(xpath = "asdf")
    List<WebElement> categories;

    @FindBy(name = "Buttons")
    WebElement buttonsCategoryCard;
}
