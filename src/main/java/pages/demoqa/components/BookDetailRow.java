package pages.demoqa.components;

import com.github.webdriverextensions.WebComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


import java.util.List;

public class BookDetailRow extends WebComponent {

    @FindBy(css = "[class='col-md-3 col-sm-12']")
    public WebElement label;

    @FindBy(css = "[class='col-md-9 col-sm-12']")
    public WebElement value;

    public String getLabel(){return label.getText();}

    public String getValue(){return value.getText();}
}
