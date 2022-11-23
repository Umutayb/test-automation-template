package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;
import java.util.List;

public class ResultModel extends WebUtilities {

    @FindBy(id = "example-modal-sizes-title-lg")
    WebElement header;

    @FindBy(css = ".modal-content tbody tr")
    List<WebElement> dataRows;

    @FindBy(id = "closeLargeModal")
    WebElement closeButton;
}
