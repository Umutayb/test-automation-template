package common;

import com.github.webdriverextensions.WebDriverExtensionFieldDecorator;
import pickleib.web.PickleibPageObject;
import pickleib.web.driver.PickleibWebDriver;

public class PageObject extends PickleibPageObject {
    public PageObject(){
        super(new WebDriverExtensionFieldDecorator(PickleibWebDriver.get()));
    }
}
