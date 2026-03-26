package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class ElementsPage extends PickleibPageObject {

    @FindBy(css = "aside nav a.nav-item")
    public List<WebElement> sidebarLinks;
}
