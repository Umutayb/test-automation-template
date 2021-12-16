package pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Utilities;
import utils.Printer;
import java.util.List;
import static resources.Colors.*;

public class ToolsUI extends Utilities {

    Printer log = new Printer(ToolsUI.class);

    @FindBy(css = "[class='accordion'] [class='element-group']")
    public List<WebElement> toolTypes;

    @FindBy(css = "[class='menu-list'] [id*='item']")
    public List<WebElement> tools;

    public void selectToolTypeNamed(String toolType){
        log.new info("Clicking tool type named "+BLUE+toolType);
        for (WebElement card:toolTypes) {
            if (card.getText().contains(toolType)){
                clickElement(card);
                return;
            }
        }
        Assert.fail(RED+"No tool type named "+BLUE+toolType+GRAY+" could be found in ToolsPage @selectToolTypeNamed"+RESET);
    }

    public void selectToolNamed(String toolType){
        log.new info("Clicking tool named "+BLUE+toolType);
        for (WebElement card:tools) {
            if (card.getText().contains(toolType)){
                clickElement(card);
                return;
            }
        }
        Assert.fail(RED+"No tool type named "+BLUE+toolType+GRAY+" could be found in ToolsPage @selectToolTypeNamed"+RESET);
    }
}
