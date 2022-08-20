package pages;

import org.openqa.selenium.support.FindBy;
import pages.components.CookieBanner;
import pages.components.CookiePopup;
import pages.components.MediaContainer;
import pages.components.OrbitBanner;
import utils.Printer;
import utils.WebUtilities;
import java.util.List;

public class LandingPage extends WebUtilities {

    Printer log = new Printer(LandingPage.class);

    @FindBy(css = "section[class='bbccookies-banner']")
    public CookieBanner cookieBanner;

    @FindBy(css = "[class='fc-dialog-container']")
    public CookiePopup cookiePopup;

    @FindBy(id = "orbit-header")
    public OrbitBanner orbitBanner;

    @FindBy(css = "section[class*='module'] li")
    List<MediaContainer> mediaContainers;

    public void clickMediaContainerTitled(String containerName){
        MediaContainer mediaContainer = acquireNamedComponentAmongst(mediaContainers, containerName, System.currentTimeMillis());
        clickElement(mediaContainer.mediaContent.title, true);
    }
}
