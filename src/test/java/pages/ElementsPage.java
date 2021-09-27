package pages;

import org.openqa.selenium.support.FindBy;
import pages.components.TextBox;
import utils.Utilities;

public class ElementsPage extends Utilities {

    @FindBy(css = "[class='text-field-container']")
    public TextBox textBox;

    public void clickSubmit(){textBox.clickSubmit();}

    public void fillNameInput(String text){textBox.fillNameInput(text);}

    public void fillEmailInput(String text){textBox.fillEmailInput(text);}

    public void fillCurrentAddressInput(String text){textBox.fillCurrentAddressInput(text);}

    public void fillPermanentAddressInput(String text){textBox.fillPermanentAddressInput(text);}
}
