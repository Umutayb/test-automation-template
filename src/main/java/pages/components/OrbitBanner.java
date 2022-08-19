package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

import java.util.List;

public class OrbitBanner extends WebUtilities {

    @FindBy(css = "#idcta-username")
    public WebElement signInButton;

    @FindBy(id = "orbit-more-button")
    public WebElement moreButton;

    @FindBy(css = "[class='orbit-header-links international'] li")
    public List<WebElement> navItems;

    @FindBy(css = "#orbit-more-drawer li")
    public List<WebElement> moreDrawerItems;
}
