package pages.demoqa.components;

import com.github.webdriverextensions.WebComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ResultModel extends WebComponent {

    @FindBy(id = "example-modal-sizes-title-lg")
    WebElement header;

    @FindBy(css = ".modal-content tbody tr")
    List<WebElement> dataRows;

    @FindBy(id = "closeLargeModal")
    WebElement closeButton;
}
