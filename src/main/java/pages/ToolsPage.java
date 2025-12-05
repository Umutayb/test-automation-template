package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class ToolsPage extends PickleibPageObject {

    @FindBy(css = ".element-group")
    List<WebElement> categories;

    @FindBy(css = ".menu-list li")
    List<WebElement> tools;

    @FindBy(css = "#login")
    WebElement loginButton;

}
