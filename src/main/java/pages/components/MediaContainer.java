package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

public class MediaContainer extends WebUtilities {

    @FindBy(css = "a")
    private WebElement articleLink;

    @FindBy(css = "img")
    public WebElement mediaImage;

    @FindBy(css = "[class='media__content']")
    public MediaContent mediaContent;

    public String getLink(){return articleLink.getAttribute("href");}

    public String getImgLink(){return mediaImage.getAttribute("src");}

    public static class MediaContent extends WebUtilities {
        @FindBy(css = "h3[class='media__title'] a")
        public WebElement title;

        @FindBy(css = "a[class*='media__tag']")
        public WebElement tag;

        public String getTagName(){return tag.getText();}

        public String getTagLink(){return tag.getAttribute("href");}

        public String getTitle(){return title.getText();}

        public String getLink(){return title.getAttribute("href");}
    }
}
