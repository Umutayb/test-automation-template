package pages;

import common.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ToolsPage extends PageObject {

    @FindBy(css = ".element-group")
    List<WebElement> categories;

    @FindBy(css = ".menu-list li")
    List<WebElement> tools;

    @FindBy(css = "#login")
    WebElement loginButton;

}
