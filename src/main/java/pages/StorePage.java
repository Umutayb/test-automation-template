package pages;

import org.openqa.selenium.support.FindBy;
import pages.components.NavigationBar;
import utils.Printer;
import utils.WebUtilities;

public class StorePage extends WebUtilities {

    Printer log = new Printer(LandingPage.class);

    @FindBy(id = "ac-globalnav")
    public NavigationBar navigationBar;



}
