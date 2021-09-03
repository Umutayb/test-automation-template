package pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Utilities;
import utils.Printer;
import java.util.List;
import static resources.Colors.*;

public class ToolsPage extends Utilities{

    Printer log = new Printer();

    @FindBy(css = "[class='accordion'] [class='element-group']")
    public List<WebElement> toolTypes;

    @FindBy(css = "[class='menu-list'] [id*='item']")
    public List<WebElement> tools;

    @FindBy(css = "button[id='submit']")
    public WebElement submitButton;

    @FindBy(css = "[id='userName']")
    public WebElement nameInput;

    @FindBy(css = "[id='userEmail']")
    public WebElement emailInput;

    @FindBy(css = "[id='currentAddress']")
    public WebElement currentAddressInput;

    @FindBy(css = "[id='permanentAddress']")
    public WebElement permanentAddressInput;

    public void clickSubmit(){
        log.print("Clicking submit button","info");
        clickElement(submitButton);
    }

    public void fillNameInput(String text){
        log.print("Filling name input with "+BLUE+text,"info");
        clearFillInput(nameInput, text, true);
    }

    public void fillEmailInput(String text){
        log.print("Filling email input with "+BLUE+text,"info");
        clearFillInput(emailInput, text, true);
    }

    public void fillCurrentAddressInput(String text){
        log.print("Filling current address input with "+BLUE+text,"info");
        clearFillInput(currentAddressInput, text, true);
    }

    public void fillPermanentAddressInput(String text){
        log.print("Filling permanent address input with "+BLUE+text,"info");
        clearFillInput(permanentAddressInput, text, true);
    }

    public void selectToolTypeNamed(String toolType){
        log.print("Clicking tool type named "+BLUE+toolType,"info");
        for (WebElement card:toolTypes) {
            if (card.getText().contains(toolType)){
                clickElement(card);
                return;
            }
        }
        Assert.fail(RED+"No tool type named "+BLUE+toolType+GRAY+" could be found in ToolsPage @selectToolTypeNamed"+RESET);
    }

    public void selectToolNamed(String toolType){
        log.print("Clicking tool named "+BLUE+toolType,"info");
        for (WebElement card:tools) {
            if (card.getText().contains(toolType)){
                clickElement(card);
                return;
            }
        }
        Assert.fail(RED+"No tool type named "+BLUE+toolType+GRAY+" could be found in ToolsPage @selectToolTypeNamed"+RESET);
    }
}
