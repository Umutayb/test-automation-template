package common;

import com.github.webdriverextensions.WebDriverExtensionFieldDecorator;
import pickleib.web.driver.PickleibWebDriver;
import pickleib.web.utilities.WebUtilities;

public class PageObject extends WebUtilities {
    public PageObject(){
        super(new WebDriverExtensionFieldDecorator(PickleibWebDriver.driver));
    }
}
