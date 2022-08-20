package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

import java.util.List;

public class NavigationBar extends WebUtilities {

    @FindBy(tagName = "li")
    public List<WebElement> categories;



}
