package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

@PageObject
public class HomePage extends PickleibPageObject {

    @FindBy(css = ".home-card")
    public List<WebElement> categoryCards;

    @FindBy(css = ".home-card h3")
    public List<WebElement> categoryNames;

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "aside nav a.nav-item")
    public List<WebElement> sidebarLinks;
}
