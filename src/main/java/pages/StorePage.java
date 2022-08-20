package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.components.NavigationBar;
import pages.components.ProductCard;
import utils.Printer;
import utils.WebUtilities;

import java.util.List;

public class StorePage extends WebUtilities {


    Printer log = new Printer(StorePage.class);

    @FindBy(id = "ac-globalnav")
    public NavigationBar navigationBar;

    @FindBy(css = ".rf-productnav-card")
    public List<ProductCard> productCards;
}
