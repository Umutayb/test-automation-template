package pages.amazon;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.utilities.WebUtilities;


import java.util.List;

public class HomePage extends WebUtilities {

    @FindBy(css = "[class*='hmenu-visible'][class*='hmenu-translateX'] li a")
    List<WebElement> menuItems;

    @FindBy(css = "[class*='hmenu-visible'] .hmenu-item")
    List<WebElement> categories;

    @FindBy(id = "nav-hamburger-menu")
    WebElement hamburgerMenuButton;
}
