package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageObject
public class ElementsPage {

    @FindBy(css = "aside nav a.nav-item")
    public List<WebElement> sidebarLinks;
}
