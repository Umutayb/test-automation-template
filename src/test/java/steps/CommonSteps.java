package steps;

import com.github.webdriverextensions.WebComponent;
import context.ContextStore;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import common.ObjectRepository;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.html5.RemoteWebStorage;
import pickleib.driver.DriverFactory;
import pickleib.enums.Direction;
import pickleib.enums.ElementState;
import pickleib.enums.Navigation;
import pickleib.utilities.CommonStepUtilities;
import pickleib.utilities.email.EmailAcquisition;
import pickleib.utilities.email.EmailInbox;
import records.Bundle;
import utils.*;
import java.util.*;

import static pickleib.driver.DriverFactory.DriverType.*;
import static pickleib.web.driver.PickleibWebDriver.driver;
import static steps.Hooks.initialiseAppiumDriver;
import static steps.Hooks.initialiseBrowser;
import static utils.StringUtilities.Color.*;


public class CommonSteps extends CommonStepUtilities<ObjectRepository> {

    EmailInbox emailInbox = new EmailInbox();

    public CommonSteps(){
        super(ObjectRepository.class);

        getInteractions(DriverFactory.DriverType.Web).setScroll(true);
        getInteractions(DriverFactory.DriverType.Mobile).setScroll(true);

        if (initialiseAppiumDriver && !initialiseBrowser) defaultPlatform = Mobile;
    }

    /**
     * Navigates to the specified URL.
     *
     * @param url The URL to navigate to.
     */
    @Given("Navigate to url: {}")
    public void getUrl(String url) {
        url = strUtils.contextCheck(url);
        webInteractions.getUrl(url);
    }

    /**
     * Navigates to the specified page by appending it to the current URL.
     *
     * @param page The page to navigate to.
     */
    @Given("Go to the {} page")
    public void toPage(String page){
        String url = driver.getCurrentUrl();
        String pageUrl = url + page;
        webInteractions.navigate(pageUrl);
    }

    /**
     * Switches to the next tab and stores the parent tab handle.
     */
    @Given("Switch to the next tab")
    public void switchTab() {
        String parentHandle = webInteractions.switchWindowByHandle(null);
        ContextStore.put("parentHandle", parentHandle);
    }

    /**
     * Switches back to the parent tab using the stored parent tab handle.
     */
    @Given("Switch back to the parent tab")
    public void switchToParentTab() {
        webInteractions.switchWindowByHandle(ContextStore.get("parentHandle").toString());
    }

    /**
     * Switches to the tab with the specified handle and stores the parent tab handle.
     *
     * @param handle The handle of the tab to switch to.
     */
    @Given("Switch to the tab with handle: {}")
    public void switchTab(String handle) {
        handle = strUtils.contextCheck(handle);
        String parentHandle = webInteractions.switchWindowByHandle(handle);
        ContextStore.put("parentHandle", parentHandle);
    }

    /**
     * Switches to the specified tab by index and stores the parent tab handle.
     *
     * @param handle The index of the tab to switch to.
     */
    @Given("Switch to the tab number {}")
    public void switchTab(Integer handle) {
        String parentHandle = webInteractions.switchWindowByIndex(handle);
        ContextStore.put("parentHandle", parentHandle);
    }

    /**
     * Navigates to the specified email path.
     * Email at a given path is treated like a web page and the driver navigates to it, enabling various webInteractionsions.
     *
     * @param url The URL of the email to navigate to.
     */
    @Given("Get email at {}")
    public void getHTML(String url) {
        url = strUtils.contextCheck(url);
        log.info("Navigating to the email @" + url);
        driver.get(url);
    }

    /**
     * Navigates to the specified environment page.
     *
     * @param environment The environment to navigate to (acceptance, test, or dev).
     */
    @Given("^Navigate to the (acceptance|test|dev) page$")
    public void getURL(ObjectRepository.Environment environment) {
        String username = PropertyUtility.getProperty("website-username");
        String password = PropertyUtility.getProperty("website-password");
        String protocol = PropertyUtility.getProperty("protocol", "https").toLowerCase();
        String baseUrl = PropertyUtility.getProperty(environment.getUrlKey());
        String url = protocol + "://" + baseUrl;

        if (ObjectRepository.environment == null && username != null && password != null) url = protocol + "://" + username + ":" + password + "@" + baseUrl;
        log.info("Navigating to " + strUtils.highlighted(BLUE, url));
        driver.get(url);
        ObjectRepository.environment = environment;
    }

    /**
     * Sets the size of the browser window.
     *
     * @param width the width of the window
     * @param height the height of the window
     */
    @Given("Set window width & height as {} & {}")
    public void setFrameSize(Integer width, Integer height) {webInteractions.setWindowSize(width,height);}

    /**
     * Adds the specified values to the browser's LocalStorage.
     *
     * @param valueTable a DataTable containing the values to add
     */
    @Given("Add the following values to LocalStorage:")
    public void addLocalStorageValues(DataTable valueTable){
        Map<String, String> form = valueTable.asMap();
        for (String valueKey: form.keySet()) {
            RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
            RemoteWebStorage webStorage = new RemoteWebStorage(executeMethod);
            LocalStorage storage = webStorage.getLocalStorage();
            storage.setItem(valueKey, strUtils.contextCheck(form.get(valueKey)));
        }
    }

    /**
     * Adds the specified cookies to the browser.
     *
     * @param cookieTable a DataTable containing the cookies to add
     */
    @Given("Add the following cookies:")
    public void addCookies(DataTable cookieTable){
        Map<String, String> cookies = cookieTable.asMap();
        for (String cookieName: cookies.keySet()) {
            Cookie cookie = new Cookie(cookieName, strUtils.contextCheck(cookies.get(cookieName)));
            driver.manage().addCookie(cookie);
        }
    }

    /**
     * Refreshes the current page.
     */
    @Given("Refresh the page")
    public void refresh() {webInteractions.refresh();}

    /**
     * Deletes all cookies from the browser.
     */
    @Given("Delete cookies")
    public void deleteCookies() {driver.manage().deleteAllCookies();}

    /**
     * Navigates the browser either forwards or backwards.
     *
     * @param direction the direction to navigate in, either "BACKWARDS" or "FORWARDS"
     */
    @Given("^Navigate browser (BACKWARDS|FORWARDS)$")
    public void browserNavigate(Navigation direction) {webInteractions.navigateBrowser(direction);}

    /**
     * Clicks the button with the specified text.
     *
     * @param text the text of the button to click
     */
    @Given("^Click button with (.+?(?:\\s+.+?)*) text(?: using (Mobile|Web) driver)?$")
    public void clickWithText(String text, String driverType) {getInteractions(getType(driverType)).clickButtonByText(text, true);}

    /**
     * Clicks the button with the specified CSS locator.
     *
     * @param text the CSS locator of the button to click
     */
    @Given("Click button with {} css locator")
    public void clickWithLocator(String text) {
        webInteractions.clickByCssSelector(text);
    }

    /**
     * Waits for the specified duration in seconds.
     *
     * @param duration the duration to wait in seconds
     */
    @Given("^Wait (\\d+) seconds(?: using (Mobile|Web) driver)?$")
    public void wait(Integer duration, String driverType) {
        getInteractions(getType(driverType)).waitForSeconds(duration);
    }

    /**
     * Scrolls in the specified direction.
     *
     * @param direction the direction to scroll in, either "up" or "down"
     */
    @Given("^Scroll (up|down)$")
    public void scrollTo(Direction direction){webInteractions.scrollOrSwipeInDirection(direction);}

    /**
     * Clicks the specified button on the page.
     *
     * @param buttonName the name of the button to click
     * @param pageName the name of the page containing the button
     */
    @Given("^(?:Click|Tap) the (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void click(String buttonName, String pageName, String driverType){
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(buttonName, pageName);
        getInteractions(getType(driverType)).clickInteraction(element, buttonName, pageName);
    }

    /**
     * Acquires the specified attribute value from the specified element on the page.
     *
     * @param attributeName the name of the attribute to acquire
     * @param elementName the name of the element to acquire the attribute value from
     * @param pageName the name of the page containing the element
     */
    @Given("^Acquire the (\\w+) attribute of (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void getAttributeValue(String attributeName, String elementName, String pageName, String driverType){
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).saveAttributeValue(element, attributeName, elementName, pageName);
    }

    /**
     * Acquires the specified attribute value from the specified element of a component on the page.
     *
     * @param attributeName the name of the attribute to acquire
     * @param elementName   the name of the element to acquire the attribute value from
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     */
    @Given("Acquire attribute {} from component element {} of {} component on the {}")
    // Use 'innerHTML' attributeName to acquire text on an element
    public void getComponentAttributeValue(String attributeName, String elementName, String componentName, String pageName) {
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.saveAttributeValue(element, attributeName, elementName, pageName);
    }

    /**
     * Centers the specified element on the page in the viewport.
     *
     * @param elementName the name of the element to center
     * @param pageName the name of the page containing the element
     */
    @Given("Center the {} on the {}")
    public void center(String elementName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromPage(elementName, pageName);
        webInteractions.center(element, elementName, pageName);
    }

    /**
     * Clicks towards the specified element on the page.
     *
     * @param elementName the name of the element to click towards
     * @param pageName the name of the page containing the element
     */
    @Given("^Click towards the (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void clickTowardsElement(String elementName, String pageName, String driverType) {
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).clickTowards(element, elementName, pageName);
    }

    /**
     * Clicks the specified element of a component on the page.
     *
     * @param elementName the name of the element to click
     * @param componentName the name of the component containing the element
     * @param pageName the name of the page containing the component
     */
    @Given("Click component element {} of {} component on the {}")
    public void componentClick(String elementName, String componentName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.clickInteraction(element, elementName, pageName);
    }

    /**
     * Centers the specified element of a component on the page in the viewport.
     *
     * @param elementName the name of the element to center
     * @param componentName the name of the component containing the element
     * @param pageName the name of the page containing the component
     */
    @Given("Center component element {} of {} component on the {}")
    public void center(String elementName, String componentName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.center(element, elementName, pageName);
    }

    /**
     * Clicks towards the specified element of a component on the page.
     *
     * @param elementName the name of the element to click towards
     * @param componentName the name of the component containing the element
     * @param pageName the name of the page containing the component
     */
    @Given("Click towards component element {} of {} component on the {}")
    public void clickTowards(String elementName, String componentName, String pageName) {
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.clickTowards(element, elementName, pageName);
    }

    /**
     * Performs a JS click on the specified element of a component on the page.
     *
     * @param elementName the name of the element to perform the JS click on
     * @param pageName the name of the page containing the component
     */
    @Given("Perform a JS click on element {} of {} component on the {}")
    public void performJSClick(String elementName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromPage(elementName, pageName);
        webInteractions.center(element, elementName, pageName);
        webInteractions.performJSClick(element, elementName, pageName);
    }

    /**
     * This method performs a JavaScript click on the given component element on the given page.
     *
     * @param elementName The name of the component element to perform the click.
     * @param componentName The name of the component that contains the element.
     * @param pageName The name of the page on which the component is located.
     */
    @Given("Perform a JS click on component element {} of {} component on the {}")
    public void performJSClick(String elementName, String componentName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.center(element, elementName, pageName);
        webInteractions.performJSClick(element, elementName, pageName);
    }

    /**
     * Clicks on the specified element if it is present on the page.
     *
     * @param elementName the name of the element to click on
     * @param pageName the name of the page containing the element
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("^If present, click the (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void clickIfPresent(String elementName, String pageName, String driverType){
        try {
            WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
            if (getInteractions(getType(driverType)).elementIs(element, elementName, pageName, ElementState.displayed))
                getInteractions(getType(driverType)).clickInteraction(element, elementName, pageName);
        }
        catch (WebDriverException ignored) {
            log.warning("The " + elementName + " was not present");
        }
    }

    /**
     * Clicks on the specified component element if it is present on the page.
     *
     * @param elementName the name of the component element to click on
     * @param componentName the name of the component containing the element
     * @param pageName the name of the page containing the component
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("If present, click component element {} of {} component on the {}")
    public void componentClickIfPresent(String elementName, String componentName, String pageName){
        try {
            WebElement element = getAcquisition(Web)
                    .acquireElementFromComponent(elementName, componentName, pageName);
            if (webInteractions.elementIs(element, elementName, pageName, ElementState.displayed))
                webInteractions.clickInteraction(element, elementName, pageName);
        } catch (WebDriverException ignored) {
            log.warning("The " + elementName + " was not present");
        }
    }

    /**
     * Clicks on the specified element from a list on the page.
     *
     * @param elementName the name of the element to click on from the list
     * @param listName the name of the list containing the element
     * @param pageName the name of the page containing the list
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Click listed element {} from {} list on the {}")
    public void clickListedButton(String elementName, String listName, String pageName){
        WebElement element = getAcquisition(Web).acquireListedElementFromPage(elementName,listName,pageName);
        webInteractions.clickInteraction(element, elementName, pageName);
    }

    /**
     * Clicks on the specified component element from a list on the page.
     *
     * @param elementName the name of the component element to click on from the list
     * @param componentName the name of the component containing the element
     * @param listName the name of the list containing the element
     * @param pageName the name of the page containing the list and component
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Click listed component element {} of {} from {} list on the {}")
    public void clickListedComponentButton(String elementName, String componentName, String listName, String pageName){
        WebElement element = getAcquisition(Web).acquireListedElementFromComponent(
                elementName,
                componentName,
                listName,
                pageName
        );
        webInteractions.clickInteraction(element, elementName, pageName);
    }

    /**
     * Scrolls through a list of component elements until an element containing a given text is found
     *
     * @param listName       the name of the list containing the element
     * @param componentdName the name of the component containing the element
     * @param pageName       the name of the page containing the list and component
     * @param elementText    the name of the component element to click on from the list
     */
    @Given("Scroll through {} list of {} component on the {} and acquire {}")
    public void scrollContainerElements(String listName, String componentdName, String pageName, String elementText) {
        componentdName = strUtils.firstLetterDeCapped(componentdName);
        listName = strUtils.firstLetterDeCapped(listName);
        pageName = strUtils.firstLetterDeCapped(pageName);
        List<WebElement> elements = getReflections(Web).getElementsFromComponent(
                listName,
                componentdName,
                pageName
        );
        log.info("Scrolling elements...");
        webInteractions.scrollInContainer(elements, elementText);
        log.info("Element named " + strUtils.markup(BLUE, elementText) + " is acquired");
    }

    /**
     * Scrolls through a list of elements until an element containing a given text is found
     *
     * @param listName      the name of the list containing the element
     * @param pageName      the name of the page containing the list and component
     * @param elementText   the name of the component element to click on from the list
     */
    @Given("Select exact element named (.+?(?:\\s+.+?)*) amongst the elements of (.+?(?:\\s+.+?)*) container list on the (.+?(?:\\s+.+?)*)(?: using (Mobile|Web) driver)?$")
    public void clickElementInContainer(String elementText, String listName, String pageName, String driverType) {
        listName = strUtils.firstLetterDeCapped(listName);
        pageName = strUtils.firstLetterDeCapped(pageName);
        List<WebElement> elements = getReflections(getType(driverType)).getElementsFromPage(
                listName,
                pageName
        );
        log.info("Acquiring element...");
        for (WebElement element : elements) {
            webInteractions.scrollWithJS(element);
            if (element.getText().contains(elementText)) {
                webInteractions.clickInteraction(element);
                break;
            }
        }
        log.info("Element named " + strUtils.markup(BLUE, elementText) + " is acquired");
    }

    /**
     * Scrolls through a list of component elements until an element containing a given text is found
     *
     * @param listName      the name of the list containing the element
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the list and component
     * @param elementText   the name of the component element to click on from the list
     */
    @Given("Select exact element named {} amongst the elements of {} container from {} component on the {}")
    public void clickElementInContainerFromComponent(String elementText, String listName, String componentName, String pageName) {
        componentName = strUtils.firstLetterDeCapped(componentName);
        listName = strUtils.firstLetterDeCapped(listName);
        pageName = strUtils.firstLetterDeCapped(pageName);
        List<WebElement> elements = getReflections(Web).getElementsFromComponent(
                listName,
                componentName,
                pageName
        );
        log.info("Acquiring element...");
        for (WebElement element : elements) {
            webInteractions.scrollWithJS(element);
            if (element.getText().contains(elementText)) {
                webInteractions.clickInteraction(element);
                break;
            }
        }
        log.info("Element named " + strUtils.markup(BLUE, elementText) + " is acquired");
    }

    /**
     * Selects a component from a list on the page and clicks on the specified element within the component.
     *
     * @param componentName the name of the component to select from the list
     * @param listName the name of the list containing the component
     * @param pageName the name of the page containing the list and component
     * @param elementName the name of the element to click on within the selected component
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Select component named {} from {} component list on the {} and click the {} element")
    public void clickButtonAmongstComponents(String componentName, String listName, String pageName, String elementName){
        WebElement element = getAcquisition(Web).acquireListedComponentElement(elementName, componentName, listName, pageName);
        webInteractions.clickInteraction(element, elementName, pageName);
    }

    /**
     * Selects an exact named component from a list on the page and clicks on the specified element within the component.
     *
     * @param componentName the exact name of the component to select from the list
     * @param listName the name of the list containing the component
     * @param pageName the name of the page containing the list and component
     * @param elementName the name of the element to click on within the selected component
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Select exact component named {} from {} component list on the {} and click the {} element")
    public void clickButtonAmongstExactNamedComponents(String componentName, String listName, String pageName, String elementName){
        WebElement element = getAcquisition(Web).acquireExactNamedListedComponentElement(elementName,
                componentName,
                listName,
                pageName);
        webInteractions.clickInteraction(element, elementName, pageName);
    }

    /**
     * Selects a component from a list on the page, and clicks on the specified listed element within the component.
     *
     * @param componentName the name of the component to select from the list
     * @param componentListName the name of the list containing the component
     * @param pageName the name of the page containing the list and component
     * @param elementName the name of the listed element to click on within the selected component
     * @param elementListName the name of the list containing the listed element
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Select component named {} from {} component list on the {} and click listed element {} of {}")
    public void clickListedButtonAmongstComponents(
            String componentName,
            String componentListName,
            String pageName,
            String elementName,
            String elementListName) {
        WebElement element = getAcquisition(Web).acquireListedElementAmongstListedComponents(
                elementName,
                elementListName,
                componentName,
                componentListName,
                pageName
        );
        webInteractions.clickInteraction(element, elementName, pageName);
    }

    /**
     * Clicks on the specified listed element that has a given attribute value for the specified attribute name in a list on the page.
     *
     * @param attributeValue the value of the attribute to match
     * @param attributeName the name of the attribute to match
     * @param listName the name of the list containing the element
     * @param pageName the name of the page containing the list
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("^Click listed attribute element that has (\\w+) value for its (\\w+) attribute from (\\w+) list on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void clickListedButtonByAttribute(String attributeValue, String attributeName, String listName, String pageName, String driverType) {
        WebElement element = getAcquisition(DriverFactory.DriverType.getType(driverType)).acquireListedElementByAttribute(attributeName, attributeValue, listName, pageName);
        getInteractions(DriverFactory.DriverType.getType(driverType)).clickInteraction(element, attributeName + " attribute named element", pageName);
    }

    /**
     * Clicks on the specified listed component element that has a given attribute value for the specified attribute name in a list on the page.
     *
     * @param componentName the name of the component containing the element
     * @param attributeValue the value of the attribute to match
     * @param attributeName the name of the attribute to match
     * @param listName the name of the list containing the element
     * @param pageName the name of the page containing the list and component
     *
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Click listed attribute element of {} component that has {} value for its {} attribute from {} list on the {}")
    public void componentClickListedButtonByAttribute(String componentName, String attributeValue, String attributeName, String listName, String pageName) {
        WebElement element = getAcquisition(Web).acquireListedComponentElementByAttribute(componentName, attributeName, attributeValue, listName, pageName);
        webInteractions.clickInteraction(element, attributeName + " attribute named element" , pageName);
    }

    /**
     * Fills in the specified input field of a listed element in a list on the page with the given text.
     *
     * @param inputName the name of the input field to fill in
     * @param listName the name of the list containing the element
     * @param pageName the name of the page containing the list
     * @param input the text to fill in the input field
     *
     * @throws WebDriverException if the input field cannot be found or filled
     */
    @Given("^Fill listed input (\\w+) from (\\w+) list on the (\\w+) with text: (.+?(?:\\s+.+?)*)(?: using (Mobile|Web) driver)?$")
    public void fillListedInput(String inputName, String listName, String pageName, String input, String driverType){
        WebElement inputElement = getAcquisition(getType(driverType)).acquireListedElementFromPage(inputName, listName, pageName);
        getInteractions(getType(driverType)).basicFill(inputElement, inputName, pageName, input, true);
    }

    /**
     * Fills in the specified input field of a component on the page with the given text.
     *
     * @param inputName the name of the input field to fill in
     * @param componentName the name of the component containing the input field
     * @param pageName the name of the page containing the component
     * @param input the text to fill in the input field
     *
     * @throws WebDriverException if the input field cannot be found or filled
     */
    @Given("^Fill input (\\w+) on the (\\w+) with (?:(un-verified|verified) )?text: (.+?(?:\\s+.+?)*)(?: using (Mobile|Web) driver)?$")
    public void fill(String inputName, String componentName, String pageName, String input, String driverType){
        WebElement inputElement = getAcquisition(getType(driverType)).acquireElementFromComponent(inputName, componentName, pageName);
        getInteractions(getType(driverType)).basicFill(inputElement, inputName, pageName, input, true);
    }

    /**
     * Fills in the specified input field of a component on the page with the given text.
     *
     * @param inputName     the name of the input field to fill in
     * @param componentName the name of the component containing the input field
     * @param pageName      the name of the page containing the component
     * @param input         the text to fill in the input field
     * @throws WebDriverException if the input field cannot be found or filled
     */
    @Given("Fill component input {} of {} component on the {} with text: {}")
    public void componentFill(String inputName, String componentName, String pageName, String input) {
        WebElement inputElement = getAcquisition(Web).acquireElementFromComponent(inputName, componentName, pageName);
        webInteractions.basicFill(inputElement, inputName, pageName, input, true);
    }

    /**
     * Fills in a form on the page with the given input values.
     *
     * @param pageName the name of the page containing the form
     * @param table a DataTable containing the input values and corresponding element names
     *
     * @throws WebDriverException if any input fields cannot be found or filled
     */
    @Given("^Fill form input on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void fillForm(String pageName, String driverType, DataTable table){
        List<Bundle<WebElement, String, String>> inputBundles = getAcquisition(DriverFactory.DriverType.getType(driverType)).acquireElementList(table.asMaps(), pageName);
        getInteractions(DriverFactory.DriverType.getType(driverType)).fillForm(inputBundles, pageName);
    }

    /**
     * Fills in a form within a component on the page with the given input values.
     *
     * @param componentName the name of the component containing the form
     * @param pageName the name of the page containing the component
     * @param table a DataTable containing the input values and corresponding element names
     *
     * @throws WebDriverException if any input fields cannot be found or filled
     */
    @Given("Fill component form input of {} component on the {}")
    public void componentFillForm(String componentName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> inputBundles = getAcquisition(Web).acquireComponentElementList(
                table.asMaps(),
                componentName,
                pageName
        );
        webInteractions.fillForm(inputBundles, pageName);
    }

    /**
     * Fills in the specified input field within an iframe on the page with the given text.
     *
     * @param inputName the name of the input field to fill in
     * @param iframeName the name of the iframe containing the input field
     * @param pageName the name of the page containing the iframe and input field
     * @param inputText the text to fill in the input field
     *
     * @throws WebDriverException if the iframe or input field cannot be found or filled
     */
    @Given("Fill iFrame element {} of {} on the {} with text: {}")
    public void fillIframeInput(String inputName, String iframeName, String pageName, String inputText){
        WebElement iframe = getAcquisition(Web).acquireElementFromPage(iframeName, pageName);
        WebElement element = getAcquisition(Web).acquireElementFromPage(inputName, pageName);
        webInteractions.fillIframeInput(iframe, element, inputName, pageName, inputText);
    }

    /**
     * Clicks on the specified i-frame element in the given page and i-frame.
     *
     * @param elementName  the name of the i-frame element to be clicked
     * @param iframeName   the name of the i-frame containing the element
     * @param pageName     the name of the page containing the i-frame
     */
    @Given("Click i-frame element {} in {} on the {}")
    public void clickIframeElement(String elementName, String iframeName,String pageName ){
        WebElement iframe = getAcquisition(Web).acquireElementFromPage(iframeName, pageName);
        WebElement element = getAcquisition(Web).acquireElementFromPage(elementName, pageName);
        webInteractions.clickIframeElement(iframe, element, elementName, iframeName, pageName);
    }

    /**
     * Fills the form inputs of the specified component in the given i-frame and page.
     *
     * @param iframeName      the name of the i-frame containing the component
     * @param componentName  the name of the component to be filled
     * @param pageName        the name of the page containing the i-frame
     * @param table           the data table containing the input values for the form inputs
     */
    @Given("Fill iframe component form input of {} component on the {}")
    public void fillFormIframe(String iframeName, String componentName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> bundles = getAcquisition(Web).acquireComponentElementList(table.asMaps(), componentName, pageName);
        WebElement iFrame = getAcquisition(Web).acquireElementFromPage(iframeName, pageName);
        webInteractions.fillFormIframe(bundles, iFrame, iframeName, pageName);
    }

    /**
     * Fills the specified input of the specified component in the given list and page with the provided text.
     *
     * @param inputName      the name of the input to be filled
     * @param listName       the name of the list containing the component
     * @param componentName the name of the component containing the input
     * @param pageName       the name of the page containing the list and component
     * @param input          the text to be entered into the input
     */
    @Given("Fill listed component input {} of {} component on the {} with text: {}")
    public void componentFillListedInput(String inputName, String listName, String componentName, String pageName, String input){
        WebElement element = getAcquisition(Web).acquireListedComponentElement(inputName, componentName, listName, pageName);
        webInteractions.basicFill(element, inputName, pageName, input, true);
    }

    /**
     * Verifies that the text of the specified element on the specified page matches the expected text.
     *
     * @param elementName    the name of the element whose text is to be verified
     * @param pageName       the name of the page containing the element
     * @param expectedText   the expected text of the element
     */
    @Given("^Verify the text of (\\w+) on the (\\w+) to be: (.+?(?:\\s+.+?)*)(?: using (Mobile|Web) driver)?$")
    public void verifyText(String elementName, String pageName, String expectedText, String driverType){
        WebElement element  = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).center(element, elementName, pageName);
        pageName = strUtils.firstLetterDeCapped(pageName);
        getInteractions(getType(driverType)).verifyText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of element list (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$") //TODO check
    public void verifyListedText(String listName, String pageName,String driverType, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = getAcquisition(getType(driverType)).acquireElementList(table.asMaps(), pageName);
        for (Bundle<WebElement, String, String> form: signForms) {
            WebElement element = getAcquisition(getType(driverType)).acquireListedElementFromPage(form.beta(), listName, pageName);
            getInteractions(getType(driverType)).basicFill(element, form.beta(), pageName, form.theta(), true);
        }
    }

    /**
     * Verifies that the text of the specified element in the specified component on the specified page matches the expected text.
     *
     * @param elementName    the name of the element whose text is to be verified
     * @param componentName the name of the component containing the element
     * @param pageName       the name of the page containing the component
     * @param expectedText   the expected text of the element
     */
    @Given("Verify text of the component element {} of {} on the {} to be: {}")
    public void componentVerifyText(String elementName, String componentName, String pageName, String expectedText){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.elementIs(element, elementName, pageName, ElementState.displayed);
        webInteractions.center(element, elementName, pageName);
        webInteractions.verifyText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of the component element {} of {} on the {} contains: {}")
    public void verifyComponentElementContainsText(String elementName, String componentName, String pageName, String expectedText) {
        WebElement element = getAcquisition(DriverFactory.DriverType.Web).acquireElementFromComponent(elementName, componentName, pageName);
        getInteractions(DriverFactory.DriverType.Web).waitUntilVisible(element, elementName, pageName);
        webInteractions.center(element, elementName, pageName);
        webInteractions.verifyElementContainsText(element, expectedText);
    }

    @Given("Verify text of component element list {} of {} on the {}") //TODO check
    public void componentVerifyListedText(String listName,String componentName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = getAcquisition(DriverFactory.DriverType.Web).acquireElementList(table.asMaps(), pageName);
        getInteractions(DriverFactory.DriverType.Web).verifyListedText(signForms, pageName);

        //List<Map<String, String>> forms = table.asMaps();
        //String elementName;
        //String expectedText;
        //for (Map<String, String> form : forms) {
        //  elementName = form.get("Input Element");
        //  expectedText = strUtils.contextCheck(form.get("Input"));
        //  log.info("Performing text verification for " +
        //          highlighted(BLUE, elementName) +
        //          highlighted(GRAY," on the ") +
        //          highlighted(BLUE, pageName) +
        //          highlighted(GRAY, " with the text: ") +
        //          highlighted(BLUE, expectedText)
        //  );
        //  pageName = strUtils.firstLetterDeCapped(pageName);
        //  componentName = strUtils.firstLetterDeCapped(componentName);
        //  List<WebElement> elements = getElementsFromComponent(listName, componentName, pageName, new ObjectRepository());
        //  WebElement element = acquireNamedElementAmongst(elements, elementName);
        //  Assert.assertEquals("The " + element.getText() + " does not contain text '",expectedText,element.getText());
        //  log.new Success("Text of the element " + element.getText() + " was verified!");
        //}
    }

    /**
     * Verifies that the specified element is present on the specified page.
     *
     * @param elementName    the name of the element to be verified
     * @param pageName       the name of the page containing the element
     */
    @Given("^Verify presence of element (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void verifyPresence(String elementName, String pageName, String driverType){
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).verifyState(element, elementName, pageName, ElementState.displayed);
    }

    /**
     * Verifies that the specified element in the specified component is present on the specified page.
     *
     * @param elementName    the name of the element to be verified
     * @param componentName the name of the component containing the element
     * @param pageName       the name of the page containing the component
     */
    @Given("Verify presence of the component element {} of {} on the {}")
    public void componentVerifyPresence(String elementName, String componentName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.verifyState(element, elementName, pageName, ElementState.displayed);
    }

    @Given("Checking the presence of the element text on the {}") //TODO check
    public void verifyPresenceText(String pageName, DataTable table) {
        List<Bundle<WebElement, String, String>> elements = getAcquisition(Web).acquireElementList(table.asMaps(), pageName);
        for (Bundle<WebElement, String, String> element : elements) {
            WebElement targetElement = getAcquisition(Web).acquireElementFromPage(element.beta(), pageName);
            getInteractions(Web).verifyState(targetElement, element.beta(), pageName, ElementState.enabled);

        }

        //String elementText;
        //List<Map<String, String>> signForms = table.asMaps();
        //List<Map<String, String>> signForms = table.asMaps();
        //for (Map<String, String> form : signForms) {
        //  elementText = strUtils.contextCheck(form.get("Input"));
        //  log.info("Performing text verification for " +
        //          highlighted(BLUE, elementText) +
        //          highlighted(GRAY, " on the ") +
        //          highlighted(BLUE, pageName)
        //  );
        //
        //  WebElement element = getElementContainingText(elementText);
        //  verifyElementState(element, ElementState.enabled);
        //  log.new Success("Presence of the element text " + elementText + " was verified!");
        //}
    }

    /**
     * Closes the current browser window.
     */
    @Given("Close the browser")
    public void closeBrowser(){
        driver.quit();
    }

    /**
     * This method verifies if the given element on the given page is in the expected state.
     *
     * @param elementName The name of the element to be verified.
     * @param pageName The name of the page on which the element is located.
     * @param expectedState The expected state of the element.
     */
    @Given("^Verify that element (\\w+) on the (\\w+) is in (\\w+) state(?: using (Mobile|Web) driver)?$")
    public void verifyState(String elementName, String pageName, ElementState expectedState, String driverType){
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).verifyState(element,elementName, pageName, expectedState);
    }

    /**
     * This method verifies if the given component element on the given page is in the expected state.
     *
     * @param elementName The name of the component element to be verified.
     * @param componentName The name of the component that contains the element.
     * @param pageName The name of the page on which the component is located.
     * @param expectedState The expected state of the element.
     */

    @Given("Verify that component element {} of {} on the {} is in {} state")
    public void componentVerifyState(String elementName, String componentName, String pageName, ElementState expectedState){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.verifyState(element,elementName, pageName, expectedState);
    }

    /**
     * This method verifies if the given component element on the given page is in the expected state, if it is present.
     *
     * @param elementName The name of the component element to be verified.
     * @param componentName The name of the component that contains the element.
     * @param pageName The name of the page on which the component is located.
     * @param expectedState The expected state of the element.
     */
    @Given("If present, verify that component element {} of {} on the {} is in {} state")
    public void verifyIfPresentElement(String elementName, String componentName, String pageName, ElementState expectedState){
        try {
            WebElement element = getAcquisition(Web)
                    .acquireElementFromComponent(elementName, componentName, pageName);
            getInteractions(Web).verifyState(element, elementName, pageName, expectedState);
        }
        catch (WebDriverException ignored) {
            log.warning("The " + elementName + " was not present");
        }
    }

    /**
     * This method waits until the given element is absent from the given page.
     *
     * @param elementName The name of the element to wait for absence.
     * @param pageName The name of the page on which the element is located.
     */
    @Given("^Wait for absence of element (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void waitUntilAbsence(String elementName, String pageName, String driverType){
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).waitUntilAbsence(element, elementName, pageName);
    }

    @Given("Wait for absence of component element {} of {} on the {}")
    public void componentWaitUntilAbsence(String elementName, String componentName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName,pageName);
        webInteractions.waitUntilAbsence(element, elementName, pageName);
    }

    /**
     * Wait for an element on a specific page to be visible.
     *
     * @param elementName the name of the element to be verified
     * @param pageName the name of the page containing the element
     */
    @Given("^Wait for element (\\w+) on the (\\w+) to be visible(?: using (Mobile|Web) driver)?$")
    public void waitUntilVisible(String elementName, String pageName,String driverType){
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName,pageName);
        getInteractions(getType(driverType)).waitUntilVisible(element, elementName, pageName);
    }

    /**
     * Wait for a component element on a specific page to be visible.
     *
     * @param elementName the name of the element to be verified
     * @param componentName the name of the component containing the element
     * @param pageName the name of the page containing the component
     */
    @Given("Wait for component element {} of {} on the {} to be visible")
    public void componentWaitUntilVisible(String elementName, String componentName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.waitUntilVisible(element, elementName, pageName);
    }

    @Given("Select component by {} named {} from {} component list on the {} and wait for {} element to be {}")
    public void waitSelectedComponentContainsAttribute(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String targetElementFieldName,
            ElementState expectedState) {
        WebComponent component = getAcquisition(DriverFactory.DriverType.Web).acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        WebElement button = getReflections(DriverFactory.DriverType.Web).getElementFromComponent(targetElementFieldName, component);
        getInteractions(DriverFactory.DriverType.Web).verifyState(button, targetElementFieldName, pageName, expectedState);
    }

    /**
     * Wait until an element on a specific page has a specific value for its attribute.
     *
     * @param elementName the name of the element to be verified
     * @param pageName the name of the page containing the element
     * @param attributeValue the expected value of the attribute
     * @param attributeName the name of the attribute to be verified
     */
    @Given("^Wait until element (\\w+) on the (\\w+) has (.+?(?:\\s+.+?)*) value for its (\\w+) attribute (?: using (Mobile|Web) driver)?$")
    public void waitUntilElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName,
            String driverType) {
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName,pageName);
        try {getInteractions(getType(driverType)).verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);}
        catch (WebDriverException ignored) {}
    }

    /**
     * Wait until a component element on a specific page has a specific value for its attribute.
     *
     * @param elementName the name of the element to be verified
     * @param componentName the name of the component containing the element
     * @param pageName the name of the page containing the component
     * @param attributeValue the expected value of the attribute
     * @param attributeName the name of the attribute to be verified
     */
    @Given("Wait until component element {} of {} on the {} has {} value for its {} attribute")
    public void componentWaitUntilElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName,pageName);
        try {webInteractions.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);}
        catch (WebDriverException ignored) {}
    }

    /**
     * Verify that an element on a specific page has a specific value for its attribute.
     *
     * @param elementName the name of the element to be verified
     * @param pageName the name of the page containing the element
     * @param attributeValue the expected value of the attribute
     * @param attributeName the name of the attribute to be verified
     */
    @Given("^Verify that element (\\w+) on the (\\w+) has (.+?(?:\\s+.+?)*) value for its (.+?(?:\\s+.+?)*) attribute(?: using (Mobile|Web) driver)?$")
    public void verifyElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName,
            String driverType) {
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName,pageName);
        getInteractions(getType(driverType)).verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    /**
     * Verify that an attribute of element on a specific page contains a specific value.
     *
     * @param attributeName  the name of the attribute to be verified
     * @param elementName    the name of the element to be verified
     * @param pageName       the name of the page containing the element
     * @param value the expected part of value of the attribute
     *
     */
    @Given("^Verify that attribute (\\w+) of element (\\w+) on the (\\w+) contains (.+?(?:\\s+.+?)*) value(?: using (Mobile|Web) driver)?$")
    public void verifyElementAttributeContainsValue(
            String attributeName,
            String elementName,
            String pageName,
            String value,
            String driverType
    ) {
        value = strUtils.contextCheck(value);
        WebElement element =  getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).verifyElementAttributeContainsValue(element, elementName, pageName, attributeName, value);
        log.info("-> " + strUtils.markup(BLUE, value));
    }

    /**
     * Verify the CSS attribute of an element on a specific page.
     *
     * @param attributeName the name of the CSS attribute to be verified
     * @param elementName the name of the element to be verified
     * @param pageName the name of the page containing the element
     * @param attributeValue the expected value of the CSS attribute
     */
    @Given("Verify {} css attribute of element {} on the {} is {} ")
    public void verifyElementColor(
            String attributeName,
            String elementName,
            String pageName,
            String attributeValue) {
        WebElement element = getAcquisition(Web).acquireElementFromPage(elementName, pageName);
        getInteractions(Web).verifyElementColor(element, attributeName, elementName, pageName, attributeValue);
    }

    /**
     * Verify that a component element on a specific page has a specific value for its attribute.
     *
     * @param elementName the name of the element to be verified
     * @param componentName the name of the component containing the element
     * @param pageName the name of the page containing the component
     * @param attributeValue the expected value of the attribute
     * @param attributeName the name of the attribute to be verified
     */
    @Given("Verify that component element {} of {} on the {} has {} value for its {} attribute")
    public void componentVerifyElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        getInteractions(Web).verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    /**
     * Verifies if a selected component in a list contains a specified attribute value.
     *
     * @param elementFieldName the field name of the element to be selected.
     * @param elementText the text of the element to be selected.
     * @param componentListName the name of the list containing the component.
     * @param pageName the name of the page containing the list.
     * @param attributeValue the value of the attribute to be verified.
     * @param attributeName the name of the attribute to be verified.
     */
    @Given("Select component by {} named {} from {} component list on the {} and verify that it has {} value for its {} attribute")
    public void verifySelectedComponentContainsAttribute(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebComponent component = getAcquisition(Web).acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        getInteractions(Web).verifyElementContainsAttribute(component, elementText, pageName, attributeName, attributeValue);
    }

    /**
     * Verifies if a selected component in a list contains a specified attribute value.
     *
     * @param elementFieldName  the field name of the element to be selected.
     * @param elementText       the text of the element to be selected.
     * @param componentListName the name of the list containing the component.
     * @param pageName          the name of the page containing the list.
     * @param attributeValue    the value of the attribute to be verified.
     * @param attributeName     the name of the attribute to be verified.
     */
    @Given("Select listed component by {} named {} from {} component list on the {} and verify that it has {} value for its {} attribute")
    public void verifySelectedComponentElementContainsAttribute(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebElement element = getAcquisition(DriverFactory.DriverType.Web).acquireExactNamedListedComponentElement(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        getInteractions(DriverFactory.DriverType.Web).verifyElementContainsAttribute(element, elementText, pageName, attributeName, attributeValue);
    }

    /**
     * Given the text of a web element inside a WebComponent,
     * this method selects the component and then clicks on a specified element within it.
     *
     * @param elementFieldName       The field name of the desired element in the WebComponent.
     * @param elementText            The text that the desired element should contain.
     * @param componentListName      The name of the list where the component can be found.
     * @param pageName               The name of the page where the component is located.
     * @param targetElementFieldName The field name of the element within the component that should be clicked.
     *                               <p>
     *                               The function first retrieves the WebComponent from the specified list on the given page,
     *                               based on the provided field name and text. Then it identifies the target element within the component using
     *                               the provided targetElementFieldName. Finally, it performs a click interaction on the identified target element.
     *                               <p>
     *                               The method assumes that the necessary components and elements exist and are accessible.
     */
    @Given("Select component by {} named {} from {} component list on the {} and click the {} element")
    public void verifySelectedComponentContainsAttribute(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String targetElementFieldName) {
        WebComponent component = getAcquisition(DriverFactory.DriverType.Web).acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        WebElement button = getReflections(DriverFactory.DriverType.Web).getElementFromComponent(targetElementFieldName, component);
        getInteractions(DriverFactory.DriverType.Web).clickInteraction(button, targetElementFieldName, pageName);
    }

    /**
     * Verify that a listed element from a specific page has a specific value for its attribute.
     *
     * @param elementName the name of the element to be verified
     * @param listName the name of the list containing the element
     * @param pageName the name of the page containing the list
     * @param attributeValue the expected value of the attribute
     * @param attributeName the name of the attribute to be verified
     */
    @Given("^Verify that element (\\w+) from (\\w+) list on the (\\w+) has (.+?(?:\\s+.+?)*) value for its (\\w+) attribute(?: using (Mobile|Web) driver)?$")
    public void verifyListedElementContainsAttribute(
            String elementName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName,
            String driverType) {
        WebElement element = getAcquisition(getType(driverType)).acquireListedElementFromPage(elementName, listName, pageName);
        getInteractions(getType(driverType)).verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    /**
     * Verifies if an element in a list contains a specified text.
     *
     * @param elementName the name of the element to be verified.
     * @param listName the name of the list containing the element.
     * @param pageName the name of the page containing the list.
     * @param expectedText the expected text to be verified in the element.
     */
    @Given("^Select listed element containing partial text (.+?(?:\\s+.+?)*) from the (\\w+) on the (\\w+) and verify its text contains (.+?(?:\\s+.+?)*)(?: using (Mobile|Web) driver)?$")
    public void verifyListedElementContainsText(
            String elementName,
            String listName,
            String pageName,
            String expectedText,
            String driverType) {
        WebElement element = getAcquisition(getType(driverType)).acquireListedElementFromPage(elementName, listName, pageName);
        getInteractions(getType(driverType)).verifyContainsText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of listed element from the {} on the {}") //TODO: fix this step
    public void verifyListedElementContainsText(String listName, String pageName, DataTable table) {
        List<Map<String, String>> signForms = table.asMaps();
        String elementText;
        for (Map<String, String> form : signForms) {
            elementText = form.get("Element Text");

            WebElement element = getAcquisition(Web).acquireListedElementFromPage(elementText, listName, pageName);
            log.info("Performing text verification for element with text '" +
                    strUtils.highlighted(BLUE, elementText) +
                    strUtils.highlighted(GRAY, "' on the ") +
                    strUtils.highlighted(BLUE, pageName)
            );
            Assert.assertTrue(
                    "The " + elementText + " does not contain text '" + elementText + "' ",
                    element.getText().contains(elementText)
            );
            log.success("Element is verified to contain '" + elementText + "' text!");
        }
    }

    /**
     * Verifies if a component element in a list contains a specified text.
     *
     * @param elementName   the name of the element to be verified.
     * @param elementName the name of the element to be verified.
     * @param pageName the name of the page containing the list.
     * @param expectedText the expected text to be verified in the element.
     */
    @Given("Verify text of (.+?(?:\\s+.+?)*) element on the (.+?(?:\\s+.+?)*) is equal to (.+?(?:\\s+.+?)*)(?: using (Mobile|Web) driver)?$")
    public void verifyElementContainsText(
            String elementName,
            String pageName,
            String expectedText,
            String driverType) {
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).verifyContainsText(element, elementName, pageName, expectedText);
    }

    /**
     * Verifies if a component element in a list contains a specified text.
     *
     * @param elementName the name of the element to be verified.
     * @param listName the name of the list containing the element.
     * @param componentName the name of the component containing the element.
     * @param pageName the name of the page containing the list.
     * @param expectedText the expected text to be verified in the element.
     */
    @Given("Verify text of listed component element {} from the {} of {} on the {} is equal to {}")
    public void verifyListedComponentElementContainsText(
            String elementName,
            String listName,
            String componentName,
            String pageName,
            String expectedText) {
        WebElement element = getAcquisition(Web).acquireListedElementFromComponent(elementName, componentName, listName, pageName);
        webInteractions.verifyContainsText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of listed component element from the {} of {} on the {}") //TODO check
    public void verifyListedComponentElementContainsText(String listName, String componentName, String pageName, DataTable table) {
        List<Map<String, String>> signForms = table.asMaps();
        String elementText;
        for (Map<String, String> form : signForms) {
            elementText = form.get("Element Text");

            WebElement element = getAcquisition(Web).acquireListedElementFromComponent(elementText, componentName, listName, pageName);
            log.info("Performing text verification for element with text '" +
                    strUtils.highlighted(BLUE, elementText) +
                    strUtils.highlighted(GRAY, "' on the ") +
                    strUtils.highlighted(BLUE, pageName)
            );
            Assert.assertTrue(
                    "The " + elementText + " does not contain text '" + elementText + "' ",
                    element.getText().contains(elementText)
            );
            log.success("Element is verified to contain '" + elementText + "' text!");
        }
    }

    /**
     * Verifies if a component element in a list contains a specified text.
     *
     * @param elementText   the text to be verified in the element.
     * @param listName      the name of the list containing the element.
     * @param componentName the name of the component containing the element.
     * @param pageName      the name of the page containing the list.
     */
    @Given("Verify presence of listed component element {} of {} list from {} component on the {}")
    public void verifyListedComponentElementContainsText(String elementText, String listName, String componentName, String pageName) {
        WebElement element = getAcquisition(Web).acquireListedElementFromComponent(elementText, componentName, listName, pageName);
        getInteractions(Web).verifyPresence(element, elementText, pageName);
    }

    /**
     * Verifies if a component element in a list contains a specified attribute value.
     *
     * @param elementName    the name of the element to be verified.
     * @param componentName  the name of the component containing the element.
     * @param listName       the name of the list containing the element.
     * @param pageName       the name of the page containing the list.
     * @param attributeValue the value of the attribute to be verified.
     * @param attributeName  the name of the attribute to be verified.
     */
    @Given("Verify that component element {} of {} from {} list on the {} has {} value for its {} attribute")
    public void componentVerifyListedElementContainsAttribute(
            String elementName,
            String componentName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebElement element = getAcquisition(DriverFactory.DriverType.Web).acquireListedElementFromComponent(elementName, componentName, listName, pageName);
        getInteractions(DriverFactory.DriverType.Web).verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    /**
     * Acquires and saves email(s) based on a specified filter and put absolute path to the saved email HTML in ContextStore("emailPath").
     *
     * @param filterType the type of filter to be applied for email acquisition.
     * @param filterKey the filter key to be used for email acquisition.
     */
    @Given("Acquire & save email with {} -> {}")
    public void acquireEmail(EmailUtilities.Inbox.EmailField filterType, String filterKey) {
        EmailAcquisition emailAcquisition = new EmailAcquisition(emailInbox);
        ContextStore.put("emailPath", emailAcquisition.acquireEmail(filterType, filterKey));
    }

    /**
     * Cleans the email inbox.
     */
    @Given("Clean the email inbox")
    public void flushEmail() {
        emailInbox.clearInbox();
    }

    /**
     * Verifies if the current URL contains a specified text.
     *
     * @param text the text to be verified in the current URL.
     */
    @Given("Verify the page is redirecting to the page {}")
    @Given("Verify the url contains with the text {}")
    public void verifyTextUrl(String text) {
        webInteractions.verifyCurrentUrl(text);
    }

    /**
     * Updates the context with a new key-value pair.
     *
     * @param key the key for the new context data.
     * @param value the value for the new context data.
     */
    @Given("Update context {} -> {}")
    public void updateContext(String key, String value){
        ContextStore.put(key, strUtils.contextCheck(value));
    }

    /**
     * Clears the input field of a page.
     *
     * @param elementName the name of the input field to be cleared.
     * @param pageName the name of the page containing the component.
     */
    @Given("^Clear input field (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void clearInputField(String elementName, String pageName, String driverType){
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        element.clear(); // TODO Inner clear() method may be needed
    }

    /**
     * Clears the input field of a component.
     *
     * @param elementName the name of the input field to be cleared.
     * @param componentName the name of the component containing the input field.
     * @param pageName the name of the page containing the component.
     */
    @Given("Clear component input field {} from {} component on the {}")
    public void componentClearInputField(String elementName, String componentName, String pageName){
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        element.clear();
    }

    /**
     * Presses a specified key on an element of a page.
     *
     * @param key         the key to be pressed.
     * @param elementName the name of the element to be pressed.
     * @param pageName    the name of the page containing the element.
     */
    @Given("^Press (\\w+) key on (\\w+) element of the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void pressKey(Keys key, String elementName, String pageName, String driverType) {
        WebElement element = getAcquisition(getType(driverType)).acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).pressKey(element, elementName, pageName, key);
    }

    /**
     * Presses a specified key on a component element.
     *
     * @param key           the key to be pressed.
     * @param elementName   the name of the component element to be pressed.
     * @param componentName the name of the component containing the element.
     * @param pageName      the name of the page containing the component.
     */
    @Given("Press {} key on component element {} from {} component on the {}")
    public void componentPressKey(Keys key, String elementName, String componentName, String pageName) {
        WebElement element = getAcquisition(Web).acquireElementFromComponent(elementName, componentName, pageName);
        getInteractions(Web).pressKey(element, elementName, pageName, key);
    }

    /**
     * Executes a JavaScript command.
     *
     * @param script the JavaScript command to be executed.
     */
    @Given("Execute JS command: {}")
    public void executeJSCommand(String script) {
        webInteractions.executeJSCommand(script);
    }

    /**
     * Listens to a specified event and prints the object obtained from the event.
     *
     * @param eventName the name of the event to be listened to.
     * @param objectScript the script used to obtain the object from the event.
     */
    @Given("Listen to {} event & print {} object")
    public void listenGetAndPrintObjectStep(String eventName, String objectScript)  {
        String listenerScript = "_ddm.listen(" + eventName + ");";
        webInteractions.listenGetAndPrintObject(listenerScript, eventName, objectScript);
    }
    @Given("Listen to {} event & verify value of {} node is {}")
    public void listenGetAndVerifyObjectStep(String eventName, String nodeSource, String expectedValue)  {
        log.info("Verifying value of '" + nodeSource + "' node");
        String listenerScript = "_ddm.listen(" + eventName + ");";
        webInteractions.listenGetAndVerifyObject(listenerScript, eventName, nodeSource, expectedValue);
    }

    /**
     * Fills the input field of a given component with a specified file.
     *
     * @param inputName the name of the input field to be filled.
     * @param componentName the name of the component containing the input field.
     * @param pageName the name of the page containing the component.
     * @param path the file path of the file to be uploaded.
     */
    @Given("Upload file on component input {} of {} component on the {} with file: {}")
    public void fillInputWithFile(String inputName, String componentName, String pageName, String path){
        WebElement inputElement = getAcquisition(Web).acquireElementFromComponent(inputName, componentName, pageName);
        getInteractions(Web).fillInputWithFile(inputElement, inputName, pageName, path);
    }

    /**
     * Listens for a specified event and verifies the values of the specified nodes in the resulting object.
     *
     * @param eventName The name of the event to listen for.
     * @param nodeTable A DataTable containing the names of the nodes to verify in the resulting object.
     */
    @Given("Listen to {} event & verify values of the following nodes")
    public void listenGetAndVerifyObjectStep(String eventName, DataTable nodeTable)  {
        String listenerScript = "_ddm.listen(" + eventName + ");";
        webInteractions.listenGetAndVerifyObject(listenerScript, eventName, nodeTable.asMaps());
    }

    /**
     * Performs text replacement on a specified attribute value by replacing a specified split value with a new value.
     *
     * @param attributeText The attribute value to be modified.
     * @param splitValue The value to be replaced within the attribute value.
     * @param attributeName The name of the attribute being modified.
     */
    @Given("Perform text replacement on {} context by replacing {} value in {}")
    public void replaceAttributeValue(String attributeText, String splitValue, String attributeName){
        attributeText = strUtils.contextCheck(attributeText);
        log.info("Acquiring " + strUtils.highlighted(BLUE,attributeText));
        ContextStore.put(attributeName, attributeText.replace(splitValue,""));
        log.info(String.valueOf(ContextStore.get(attributeName)));
    }

    /**
     * Selects a component from a specified component list in a page object, based on the text of a child element, and performs webInteractionsions with the second child element based on provided specifications.
     *
     * @param componentListName The name of the component list in the page object.
     * @param pageName The name of the page object.
     * @param table A DataTable containing the specifications for each second child element to be webInteractionsed with, including the selector text, selector element name, target element name, and webInteractionsion type.
     */
    @Given("Select component amongst {} list by child element and webInteractions with a second child on the {}")
    public void selectComponentByChildAndInteractWithSecondChild(
            String componentListName,
            String pageName,
            DataTable table
    ){
        List<Bundle<String, WebElement, Map<String, String>>> bundles =
                getAcquisition(Web).selectChildElementsFromComponentsBySecondChildText(
                        table.asMaps(),
                        componentListName,
                        pageName
                );
        webInteractions.bundleInteraction(bundles, pageName);
    }

    /**
     * Performs interactions with elements on a specified page object, based on the provided specifications.
     *
     * @param pageName       The name of the page object.
     * @param specifications A DataTable containing the specifications for each element to be interacted with, including the element name and interaction type.
     */
    @Given("^Interact with element on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void pageElementInteraction(String pageName, String driverType,DataTable specifications) {
        List<Bundle<String, WebElement, Map<String, String>>> bundles = getAcquisition(getType(driverType)).acquireElementBundlesFromPage(
                pageName,
                specifications.asMaps()
        );
        getInteractions(getType(driverType)).bundleInteraction(bundles, pageName);
    }

    /**
     * Performs interactions with the elements of a specified component in a page object, based on the provided specifications.
     *
     * @param componentFieldName The name of the component field in the page object.
     * @param pageName           The name of the page object.
     * @param specifications     A DataTable containing the specifications for each element to be interacted with, including the element name and interaction type.
     */
    @Given("Interact with component element of {} component on the {}")
    public void componentElementInteraction(
            String componentFieldName,
            String pageName,
            DataTable specifications
    ) {
        List<Bundle<String, WebElement, Map<String, String>>> bundles = getAcquisition(DriverFactory.DriverType.Web).acquireElementBundlesFromComponent(
                componentFieldName,
                pageName,
                specifications.asMaps()
        );
        getInteractions(DriverFactory.DriverType.Web).bundleInteraction(bundles, pageName);
    }

    /**
     * Performs webInteractionsions with elements on a specified page object, based on the provided specifications.
     *
     * @param pageName The name of the page object.
     * @param specifications A DataTable containing the specifications for each element to be webInteractionsed with, including the element name and webInteractionsion type.
     */
    @Given("Interact with element on the {}")
    public void pageElementInteraction(String pageName, DataTable specifications){
        List<Bundle<String, WebElement, Map<String, String>>> bundles = getAcquisition(Web).acquireElementBundlesFromPage(
                pageName,
                specifications.asMaps()
        );
        webInteractions.bundleInteraction(bundles, pageName);
    }
}