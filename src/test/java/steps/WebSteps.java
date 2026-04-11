package steps;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pickleib.runner.PickleibRunner;
import pickleib.web.driver.PickleibWebDriver;

public class WebSteps {

    @Given("^Hover over the (\\w+) on the (\\w+)$")
    public void hoverOverElement(String elementName, String pageName) {
        WebElement element = PickleibRunner.getRegistry().acquireElementFromPage(elementName, pageName);
        new Actions(PickleibWebDriver.get()).moveToElement(element).perform();
    }
}
