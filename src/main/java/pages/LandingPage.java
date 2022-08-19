package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.components.CookieBanner;
import pages.components.OrbitBanner;
import utils.Printer;
import utils.WebUtilities;

import java.util.List;

public class LandingPage extends WebUtilities {

    Printer log = new Printer(LandingPage.class);

    @FindBy(css = "section[class='bbccookies-banner']")
    public CookieBanner cookieBanner;

    @FindBy(id = "orbit-header")
    public OrbitBanner orbitBanner;

}
