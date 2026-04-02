package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

@PageObject
public class InteractionsPage extends PickleibPageObject {

    @FindBy(css = "aside nav a.nav-item")
    public List<WebElement> sidebarLinks;
}
