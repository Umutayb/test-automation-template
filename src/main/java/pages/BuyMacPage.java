package pages;

import org.openqa.selenium.support.FindBy;
import pages.components.NavigationBar;
import pages.components.PageShortcuts;
import pages.components.ProductCard;
import pages.components.CardsShelf;
import utils.WebUtilities;
import java.util.List;
import utils.Printer;

public class BuyMacPage extends WebUtilities {

    Printer log = new Printer(BuyMacPage.class);

    @FindBy(id = "ac-globalnav")
    public NavigationBar navigationBar;

    @FindBy(className = "rf-navbar-content-wrapper")
    public PageShortcuts pageShortcuts;

    @FindBy(className = "rs-cardsshelf")
    public List<CardsShelf> cardsShelf;

    @FindBy(css = ".rf-cards-scroller-itemview")
    public List<ProductCard> products;
}
