package pages.amazon;

import common.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


import java.util.List;

public class HomePage extends PageObject {

    @FindBy(css = "[class*='hmenu-visible'][class*='hmenu-translateX'] li a")
    List<WebElement> menuItems;

    @FindBy(css = "[class*='hmenu-visible'] .hmenu-item")
    List<WebElement> categories;

    @FindBy(id = "nav-hamburger-menu")
    WebElement hamburgerMenuButton;
}
