package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;
import java.util.List;

public class DatePicker extends WebUtilities {

    @FindBy(css = ".react-datepicker__month-select option")
    List<WebElement> months;

    @FindBy(css = ".react-datepicker__year-select option")
    List<WebElement> years;

    @FindBy(css = ".react-datepicker__month [role='option']")
    List<WebElement> days;
}
