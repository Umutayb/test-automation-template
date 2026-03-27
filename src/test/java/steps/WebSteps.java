package steps;

import common.ObjectRepository;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pickleib.utilities.element.acquisition.design.PageObjectModel;
import pickleib.web.driver.PickleibWebDriver;

public class WebSteps {

    PageObjectModel<ObjectRepository> objectRepository =
            new PageObjectModel<>(ObjectRepository.class);

    @Given("^Hover over the (\\w+) on the (\\w+)$")
    public void hoverOverElement(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        new Actions(PickleibWebDriver.get()).moveToElement(element).perform();
    }
}
