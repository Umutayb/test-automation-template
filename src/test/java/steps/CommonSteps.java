package steps;

import collections.Bundle;
import com.github.webdriverextensions.WebComponent;
import common.ObjectRepository;
import context.ContextStore;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import pickleib.driver.DriverFactory;
import pickleib.enums.Direction;
import pickleib.enums.ElementState;
import pickleib.enums.Navigation;
import pickleib.exceptions.PickleibVerificationException;
import pickleib.mobile.driver.PickleibAppiumDriver;
import pickleib.mobile.interactions.PlatformInteractions;
import pickleib.utilities.element.acquisition.ElementAcquisition;
import pickleib.utilities.interfaces.PolymorphicUtilities;
import pickleib.utilities.steps.PageObjectStepUtilities;
import pickleib.web.driver.PickleibWebDriver;
import pickleib.web.interactions.WebInteractions;
import java.util.*;

import static pickleib.driver.DriverFactory.DriverType.getType;
import static pickleib.driver.DriverFactory.DriverType.selenium;
import static pickleib.utilities.platform.PlatformUtilities.isPlatformElement;
import static pickleib.web.driver.WebDriverFactory.BrowserType.CHROME;
import static steps.Hooks.*;
import static utils.StringUtilities.Color.*;
import static utils.StringUtilities.*;

public class CommonSteps extends PageObjectStepUtilities<ObjectRepository> {

    WebInteractions webInteractions;
    PlatformInteractions mobileInteractions;

    public CommonSteps() {
        super(ObjectRepository.class, initialiseAppiumDriver, initialiseBrowser);
        if (initialiseAppiumDriver)
            mobileInteractions = new PlatformInteractions();
        if (initialiseBrowser)
            webInteractions = new WebInteractions();
    }

    /**
     * Sets the default platform type for subsequent operations.
     *
     * <p>
     * This method is typically used in conjunction with Gherkin-based feature files in a behavior-driven development
     * (BDD) setup. The provided annotation denotes the Gherkin step that matches this method.
     * </p>
     *
     * <p><strong>Usage Example in Feature File:</strong></p>
     * <pre>
     * Given Set default platform as Mobile
     * </pre>
     *
     * @param type The platform type which can be either {@link DriverFactory.DriverType#appium} or
     *             {@link DriverFactory.DriverType#selenium}.
     */
    @Given("^Set default platform as (Mobile|Web)$")
    public void setDefaultPlatform(DriverFactory.DriverType type) {
        defaultPlatform = type;
    }

    /**
     * Navigates to the specified URL.
     *
     * @param url The URL to navigate to.
     */
    @Given("Navigate to url: {}")
    public void getUrl(String url) {
        url = contextCheck(url);
        webInteractions.getUrl(url);
    }

    /**
     * Switches to next active window.
     *
     */
    @Given("Switch to the next active window")
    public void switchToNextActiveWindow() {
        PickleibAppiumDriver.get().switchTo().window(PickleibAppiumDriver.get().getWindowHandles().stream().findAny().orElseGet(null));
    }

    /**
     * Navigates to the specified page by appending it to the current URL.
     *
     * @param page The page to navigate to.
     */
    @Given("Go to the {} page")
    public void toPage(String page) {
        String url = PickleibWebDriver.get().getCurrentUrl();
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
        handle = contextCheck(handle);
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
     * Email on a given path is treated like a web page and the driver navigates to it, enabling various interactions.
     *
     * @param url The URL of the email to navigate to.
     */
    @Given("Get email at {}")
    public void getHTML(String url) {
        url = contextCheck(url);
        log.info("Navigating to the email @" + url);
        if (!isWebUI) {
            initialiseBrowser = true;
            webInteractions = new WebInteractions();
            PickleibWebDriver.initialize(CHROME);
            webInteractions.driver = PickleibWebDriver.get();
        }
        webInteractions.driver.get(url);
    }

    /**
     * Save current url to context.
     */
    @Given("Save current url to context")
    public void saveCurrentUrl() {
        String currentUrl = webInteractions.driver.getCurrentUrl();
        ContextStore.put("currentUrl", currentUrl);
        log.info("Current URL is saved to context " + currentUrl);
    }

    /**
     * Navigates to the specified environment page.
     *
     * @param environment The environment to navigate to (acceptance, test, or dev).
     */
    @Given("^Navigate to the (acceptance|test|dev) page$")
    public void getURL(ObjectRepository.Environment environment) {
        String username = ContextStore.get("website-username");
        String password = ContextStore.get("website-password");
        String protocol = ContextStore.get("protocol", "https").toLowerCase();
        String baseUrl = ContextStore.get(environment.getUrlKey());
        String url = protocol + "://" + baseUrl;

        // appending username, password with URL
        // We check if the env is null so that we do not set credentials twice
        if (ObjectRepository.environment == null && username != null && password != null)
            url = protocol + "://" + username + ":" + password + "@" + baseUrl;
        log.info("Navigating to " + highlighted(BLUE, url));
        webInteractions.driver.get(url);
        ObjectRepository.environment = environment;
    }

    /**
     * Sets the size of the browser window.
     *
     * @param width  the width of the window
     * @param height the height of the window
     */
    @Given("Set window width & height as {} & {}")
    public void setFrameSize(Integer width, Integer height) {
        webInteractions.setWindowSize(width, height);
    }

    /**
     * Adds the specified values to the browser's LocalStorage.
     *
     * @param valueTable a DataTable containing the values to add
     */
    @Given("Add the following values to LocalStorage:")
    public void addLocalStorageValues(DataTable valueTable) {
        webInteractions.addLocalStorageValues(valueTable.asMap());
    }

    /**
     * Adds the specified cookies to the browser.
     *
     * @param cookieTable a DataTable containing the cookies to add
     */
    @Given("Add the following cookies:")
    public void addCookies(DataTable cookieTable) {
        webInteractions.addCookies(cookieTable.asMap());
    }

    /**
     * Updates the specified cookie to the browser.
     *
     * @param cookieName  Target cookie to be updated
     * @param cookieValue New cookie value
     */
    @Given("Update value to {} for cookie named {}")
    public void updateCookie(String cookieValue, String cookieName) {
        webInteractions.updateCookies(cookieValue, cookieName);
    }

    /**
     * Refreshes the current page.
     */
    @Given("Refresh the page")
    public void refresh() {
        webInteractions.refresh();
    }

    /**
     * Deletes all cookies from the browser.
     */
    @Given("Delete cookies")
    public void deleteCookies() {
        webInteractions.driver.manage().deleteAllCookies();
    }

    /**
     * Navigates the browser either forwards or backwards.
     *
     * @param direction the direction to navigate in, either "BACKWARDS" or "FORWARDS"
     */
    @Given("^Navigate browser (BACKWARDS|FORWARDS)$")
    public void browserNavigate(Navigation direction) {
        webInteractions.navigateBrowser(direction);
    }

    /**
     * Clicks a button with the specified text using the specified driver type.
     *
     * @param text       The text of the button to be clicked.
     * @param driverType The type of driver to be used, either "Mobile" or "Web".
     *                   If not provided or unrecognized, the default driver type will be used.
     */
    @Given("^(?:Click|Tap) button with (.+?(?:\\s+.+?)*) text(?: using (Mobile|Web) driver)?$")
    public void clickWithText(String text, String driverType) {
        getInteractions(getType(driverType)).clickByText(text);
    }

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
    @Given("^Wait (\\d+) seconds$")
    public void wait(double duration) {
        PolymorphicUtilities.waitFor(duration);
    }

    /**
     * Scrolls in the specified direction.
     *
     * @param direction  The direction to scroll in, either "up" or "down".
     * @param driverType The type of driver to be used, either "Mobile" or "Web".
     *                   If not provided or unrecognized, the default driver type will be used.
     */
    @Given("^(?:Scroll|Swipe) (up|down|left|right) using (Mobile|Web) driver$")
    public void scrollTo(Direction direction, DriverFactory.DriverType driverType) {
        getInteractions(driverType).scrollInDirection(direction);
    }

    /**
     * Scrolls or swipes until the specified element from the given list is found on the specified screen.
     *
     * @param elementText The text of the element to be found.
     * @param listName    The name of the list where the element is located.
     * @param screenName  The name of the screen where the element is to be found.
     */
    @Given("^(?:Scroll|Swipe) until listed (.+?(?:\\s+.+?)*) element from (\\w+) list is found on the (\\w+)$")
    public void swipeUntilElementFound(String elementText, String listName, String screenName) {
        List<WebElement> elements = pageObjectReflections.getElementsFromPage(listName, screenName);
        getInteractions(elements.get(0)).scrollInList(elementText, elements);
    }

    /**
     * Scrolls or swipes until the specified element from the given list is found on the specified screen.
     *
     * @param elementText The text of the element to be found.
     */
    @Given("^(?:Scroll|Swipe) until element with exact text (.+?(?:\\s+.+?)*) is found using (Web|Mobile) driver$")
    public void swipeUntilElementFound(String elementText, DriverFactory.DriverType driverType) {
        getInteractions(driverType).scrollUntilFound(elementText);
    }

    /**
     * Clicks the specified button on the page.
     *
     * @param buttonName the name of the button to click
     * @param pageName   the name of the page containing the button
     */
    @Given("^(?:Click|Tap) the (\\w+) on the (\\w+)$")
    public void click(String buttonName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(buttonName, pageName);
        getInteractions(element).clickElement(element, buttonName, pageName, !isPlatformElement(element));
    }

    /**
     * Acquires the specified attribute value from the specified element on the page.
     *
     * @param attributeName the name of the attribute to acquire
     * @param elementName   the name of the element to acquire the attribute value from
     * @param pageName      the name of the page containing the element
     */
    @Given("^Acquire the (\\w+) attribute of (\\w+) on the (\\w+)$")
    public void getAttributeValue(String attributeName, String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).saveAttributeValue(element, attributeName, elementName, pageName);
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
    public void BookingOverviewPagegetComponentAttributeValue(String attributeName, String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.saveAttributeValue(element, attributeName, elementName, pageName);
    }

    /**
     * Centers the specified element on the page in the viewport.
     *
     * @param elementName the name of the element to center
     * @param pageName    the name of the page containing the element
     */
    @Given("Center the {} on the {}")
    public void center(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).centerElement(element, elementName, pageName);
    }

    /**
     * Centers the specified element on the page in the viewport.
     *
     * @param elementName     the name of the element to center
     * @param elementListName the name of the element list that includes the element
     * @param pageName        the name of the page containing the element
     */
    @Given("Center element named {} on the {} from {}")
    public void centerListedElement(String elementName, String elementListName, String pageName) {
        elementName = contextCheck(elementName);
        WebElement element = objectRepository.acquireListedElementFromPage(elementName, elementListName, pageName);
        getInteractions(element).centerElement(element, elementName, pageName);
    }

    /**
     * Clicks towards the specified element on the specified page.
     *
     * @param elementName The name or identifier of the element to click towards.
     * @param pageName    The name or identifier of the page where the element is located.
     */
    @Given("^^(?:Click|Tap) towards the (\\w+) on the (\\w+)$")
    public void clickTowardsElement(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).clickTowards(element, elementName, pageName);
    }

    /**
     * Clicks the specified element of a component on the page.
     *
     * @param elementName   the name of the element to click
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     */
    @Given("Click component element {} of {} component on the {}")
    public void componentClick(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.clickElement(element, elementName, pageName, true);
    }

    /**
     * Centers the specified element of a component on the page in the viewport.
     *
     * @param elementName   the name of the element to center
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     */
    @Given("Center component element {} of {} component on the {}")
    public void center(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.centerElement(element, elementName, pageName);
    }

    /**
     * Clicks towards the specified element of a component on the page.
     *
     * @param elementName   the name of the element to click towards
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     */
    @Given("Click towards component element {} of {} component on the {}")
    public void clickTowards(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.clickTowards(element, elementName, pageName);
    }

    /**
     * Performs a JS click on the specified element of a component on the page.
     *
     * @param elementName the name of the element to perform the JS click on
     * @param pageName    the name of the page containing the component
     */
    @Given("Perform a JS click on element named {} on the {}")
    public void performJSClick(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        webInteractions.performJSClick(element, elementName, pageName);
    }

    /**
     * Performs a JS click on the specified element of a component on the page.
     *
     * @param elementName the name of the element to perform the JS click on
     * @param listName    The name of the list that contains the element.
     * @param pageName    the name of the page containing the component
     */
    @Given("Perform JS click on element named {} on the {} list from {}")
    public void performListedJSClick(String elementName, String listName, String pageName) {
        WebElement element = objectRepository.acquireListedElementFromPage(elementName, listName, pageName);
        webInteractions.performJSClick(element, elementName, pageName);
    }

    /**
     * This method performs a JavaScript click on the given component element on the given page.
     *
     * @param elementName   The name of the component element to perform the click.
     * @param componentName The name of the component that contains the element.
     * @param pageName      The name of the page on which the component is located.
     */
    @Given("Perform a JS click on component element {} of {} component on the {}")
    public void componentPerformJSClick(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.performJSClick(element, elementName, pageName);
    }

    /**
     * Clicks towards the specified element on the specified page using the specified driver type.
     *
     * @param elementName   The name or identifier of the element to click towards.
     * @param componentName The name or identifier of the component where the element is located.
     * @param listName      The name or identifier of the list where the element is included.
     * @param pageName      The name or identifier of the page where the element is located.
     */
    @Given("Perform the JS click on element named {} of {} from {} list on the {}")
    public void performListedComponentJSClick(String elementName, String componentName, String listName, String pageName) {
        WebElement element = objectRepository.acquireListedElementFromComponent(
                elementName,
                componentName,
                listName,
                pageName
        );
        webInteractions.performJSClick(element, elementName, pageName);
    }

    /**
     * Clicks on the specified element if it is present on the page.
     *
     * @param elementName the name of the element to click on
     * @param pageName    the name of the page containing the element
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("^If present, (?:click|tap) the (\\w+) on the (\\w+)$")
    public void clickIfPresent(String elementName, String pageName) {
        try {
            WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
            if (getInteractions(element).elementIs(element, elementName, pageName, ElementState.displayed))
                getInteractions(element).clickElement(element, elementName, pageName);
        } catch (WebDriverException ignored) {
            log.warning("The " + elementName + " was not present");
        }
    }

    /**
     * Clicks on the specified component element if it is present on the page.
     *
     * @param elementName   the name of the component element to click on
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("If present, click component element {} of {} component on the {}")
    public void componentClickIfPresent(String elementName, String componentName, String pageName) {
        try {
            WebElement element = objectRepository
                    .acquireElementFromComponent(elementName, componentName, pageName);
            if (webInteractions.elementIs(element, elementName, pageName, ElementState.displayed))
                webInteractions.clickElement(element, elementName, pageName);
        } catch (WebDriverException ignored) {
            log.warning("The " + elementName + " was not present");
        }
    }

    /**
     * Clicks on the specified element from a list on the page.
     *
     * @param elementName the name of the element to click on from the list
     * @param listName    the name of the list containing the element
     * @param pageName    the name of the page containing the list
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("^(?:Click|Tap) listed element (.+?(?:\\s+.+?)*) from (\\w+) list on the (\\w+)$")
    public void clickListedButton(String elementName, String listName, String pageName) {
        List<WebElement> elements = pageObjectReflections.getElementsFromPage(listName, pageName);
        WebElement element = getInteractions(elements.get(0)).acquireNamedElementAmongst(elements, elementName, pageName);
        getInteractions(element).clickElement(element, elementName, pageName);
    }

    /**
     * Clicks on the specified component element from a list on the page.
     *
     * @param elementName   the name of the component element to click on from the list
     * @param componentName the name of the component containing the element
     * @param listName      the name of the list containing the element
     * @param pageName      the name of the page containing the list and component
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Click listed component element {} of {} from {} list on the {}")
    public void clickListedComponentButton(String elementName, String componentName, String listName, String pageName) {
        WebElement element = objectRepository.acquireListedElementFromComponent(
                elementName,
                componentName,
                listName,
                pageName
        );
        webInteractions.clickElement(element, elementName, pageName);
    }

    /**
     * Scrolls through a list until an element containing a given text is found
     *
     * @param listName    the name of the list containing the element
     * @param pageName    the name of the page containing the list and component
     * @param elementText the name of the component element to click on from the list
     */
    @Given("Scroll in (\\w+) container and click (.+?(?:\\s+.+?)*) element from (\\w+) list on (\\w+)$")
    public void scrollContainerElements(String containerName, String elementText, String listName, String pageName) {
        containerName = firstLetterDeCapped(containerName);
        listName = firstLetterDeCapped(listName);
        pageName = firstLetterDeCapped(pageName);
        elementText = contextCheck(elementText);
        List<WebElement> elements = pageObjectReflections.getElementsFromPage(listName, pageName);
        WebElement container = pageObjectReflections.getElementFromPage(containerName, pageName);
        log.info("Scrolling elements...");
        WebElement targetElement = webInteractions.scrollInContainer(container, elements, elementText);
        webInteractions.clickElement(targetElement);
    }

    @Given("Scroll in (\\w+) container and click (.+?(?:\\s+.+?)*) element from (\\w+) list of (\\w+) component on (\\w+)$")
    public void scrollContainerElementsFromComponent(String containerName, String elementText, String listName, String componentName, String pageName) {
        containerName = firstLetterDeCapped(containerName);
        componentName = firstLetterDeCapped(componentName);
        listName = firstLetterDeCapped(listName);
        pageName = firstLetterDeCapped(pageName);
        elementText = contextCheck(elementText);
        List<WebElement> elements = pageObjectReflections.getElementsFromComponent(listName, componentName, pageName);
        WebElement container = pageObjectReflections.getElementFromComponent(containerName, componentName, pageName);
        log.info("Scrolling elements...");
        WebElement targetElement = webInteractions.scrollInContainer(container, elements, elementText);
        webInteractions.clickElement(targetElement);
    }

    /**
     * Scrolls through a list of component elements until an element containing a given text is found
     *
     * @param listName      the name of the list containing the element
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the list and component
     * @param elementText   the name of the component element to click on from the list
     */
    @Given("Scroll through {} list of {} component on the {} and acquire {}")
    public void scrollComponentContainerElements(String listName, String componentName, String pageName, String elementText) {
        componentName = firstLetterDeCapped(componentName);
        listName = firstLetterDeCapped(listName);
        pageName = firstLetterDeCapped(pageName);
        List<WebElement> elements = pageObjectReflections.getElementsFromComponent(
                listName,
                componentName,
                pageName
        );
        log.info("Scrolling elements...");
        webInteractions.scrollInList(elementText, elements);
        log.info("Element named " + markup(BLUE, elementText) + " is acquired");
    }

    /**
     * Scrolls through a list of elements until an element containing a given text is found
     *
     * @param listName    the name of the list containing the element
     * @param pageName    the name of the page containing the list and component
     * @param elementText the name of the component element to click on from the list
     */
    @Given("Scroll through (\\w+) list on the (\\w+) and acquire (.+?(?:\\s+.+?)*)$")
    public void scrollContainerElements(String listName, String pageName, String elementText) {
        listName = firstLetterDeCapped(listName);
        pageName = firstLetterDeCapped(pageName);
        elementText = contextCheck(elementText);
        List<WebElement> elements = pageObjectReflections.getElementsFromPage(
                listName,
                pageName
        );
        log.info("Scrolling elements...");
        webInteractions.scrollInList(elementText, elements);
        log.info("Element named " + markup(BLUE, elementText) + " is acquired");
    }

    /**
     * Scrolls through a list of elements until an element containing a given text is found
     *
     * @param listName    the name of the list containing the element
     * @param pageName    the name of the page containing the list and component
     * @param elementText the name of the component element to click on from the list
     */
    @Given("Select exact element named (.+?(?:\\s+.+?)*) amongst the elements of (.+?(?:\\s+.+?)*) container list on the (.+?(?:\\s+.+?)*)$")
    public void clickElementInContainer(String elementText, String listName, String pageName) {
        listName = firstLetterDeCapped(listName);
        pageName = firstLetterDeCapped(pageName);
        List<WebElement> elements = pageObjectReflections.getElementsFromPage(
                listName,
                pageName
        );
        getInteractions(elements.get(0)).scrollInList(elementText, elements);
        WebElement element = ElementAcquisition.acquireNamedElementAmongst(elements, elementText);
        getInteractions(element).clickElement(element, elementText, pageName);
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
        componentName = firstLetterDeCapped(componentName);
        listName = firstLetterDeCapped(listName);
        pageName = firstLetterDeCapped(pageName);
        List<WebElement> elements = pageObjectReflections.getElementsFromComponent(
                listName,
                componentName,
                pageName
        );
        log.info("Acquiring element...");
        webInteractions.clickElement(ElementAcquisition.acquireNamedElementAmongst(elements, elementText));
        log.info("Element named " + markup(BLUE, elementText) + " is acquired");
    }

    /**
     * Selects a component from a list on the page and clicks on the specified element within the component.
     *
     * @param componentName the name of the component to select from the list
     * @param listName      the name of the list containing the component
     * @param pageName      the name of the page containing the list and component
     * @param elementName   the name of the element to click on within the selected component
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Select component named {} from {} component list on the {} and click the {} element")
    public void clickButtonAmongstComponents(String componentName, String listName, String pageName, String elementName) {
        WebElement element = objectRepository.acquireListedComponentElement(elementName, componentName, listName, pageName);
        webInteractions.clickElement(element, elementName, pageName);
    }

    /**
     * Selects an exact named component from a list on the page and clicks on the specified element within the component.
     *
     * @param componentName the exact name of the component to select from the list
     * @param listName      the name of the list containing the component
     * @param pageName      the name of the page containing the list and component
     * @param elementName   the name of the element to click on within the selected component
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Select exact component named {} from {} component list on the {} and click the {} element")
    public void clickButtonAmongstExactNamedComponents(String componentName, String listName, String pageName, String elementName) {
        WebComponent component = objectRepository.acquireExactNamedListedComponent(
                elementName,
                componentName,
                listName,
                pageName
        );
        WebElement element = pageObjectReflections.getElementFromComponent(elementName, component);
        webInteractions.clickElement(element, elementName, pageName);
    }

    /**
     * Selects a component from a list on the page, and clicks on the specified listed element within the component.
     *
     * @param componentName     the name of the component to select from the list
     * @param componentListName the name of the list containing the component
     * @param pageName          the name of the page containing the list and component
     * @param elementName       the name of the listed element to click on within the selected component
     * @param elementListName   the name of the list containing the listed element
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Select component named {} from {} component list on the {} and click listed element {} of {}")
    public void clickListedButtonAmongstComponents(
            String componentName,
            String componentListName,
            String pageName,
            String elementName,
            String elementListName) {
        WebElement element = objectRepository.acquireListedElementAmongstListedComponents(elementName, elementListName, componentName, componentListName, pageName);
        webInteractions.clickElement(element, elementName, pageName);
    }

    /**
     * Clicks on the specified listed element that has a given attribute value for the specified attribute name in a list on the page.
     *
     * @param attributeValue the value of the attribute to match
     * @param attributeName  the name of the attribute to match
     * @param listName       the name of the list containing the element
     * @param pageName       the name of the page containing the list
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("^Click listed attribute element that has (\\w+) value for its (\\w+) attribute from (\\w+) list on the (\\w+)$")
    public void clickListedButtonByAttribute(String attributeValue, String attributeName, String listName, String pageName) {
        WebElement element = objectRepository.acquireListedElementByAttribute(attributeName, attributeValue, listName, pageName);
        getInteractions(element).clickElement(element, attributeName + " attribute named element", pageName);
    }

    /**
     * Clicks on the specified listed component element that has a given attribute value for the specified attribute name in a list on the page.
     *
     * @param componentName  the name of the component containing the element
     * @param attributeValue the value of the attribute to match
     * @param attributeName  the name of the attribute to match
     * @param listName       the name of the list containing the element
     * @param pageName       the name of the page containing the list and component
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("Click listed attribute element of {} component that has {} value for its {} attribute from {} list on the {}")
    public void componentClickListedButtonByAttribute(String componentName, String attributeValue, String attributeName, String listName, String pageName) {
        WebElement element = objectRepository.acquireListedComponentElementByAttribute(componentName, attributeName, attributeValue, listName, pageName);
        webInteractions.clickElement(element, attributeName + " attribute named element", pageName);
    }

    /**
     * Fills in the specified input field of a listed element in a list on the page with the given text.
     *
     * @param inputName the name of the input field to fill in
     * @param listName  the name of the list containing the element
     * @param pageName  the name of the page containing the list
     * @param input     the text to fill in the input field
     * @throws WebDriverException if the input field cannot be found or filled
     */
    @Given("^Fill listed input (\\w+) from (\\w+) list on the (\\w+) with text: (.+?(?:\\s+.+?)*)$")
    public void fillListedInput(String inputName, String listName, String pageName, String input) {
        WebElement inputElement = objectRepository.acquireListedElementFromPage(inputName, listName, pageName);
        PolymorphicUtilities interactions = getInteractions(inputElement);
        interactions.fillInputElement(
                inputElement,
                inputName,
                pageName,
                input,
                !isPlatformElement(inputElement),
                !isPlatformElement(inputElement),
                true
        );
    }

    /**
     * Fills in the specified input field of on the page with the given text.
     *
     * @param inputName the name of the input field to fill in
     * @param pageName  the name of the page containing the component
     * @param input     the text to fill in the input field
     * @throws WebDriverException if the input field cannot be found or filled
     */
    @Given("^Fill input (\\w+) on the (\\w+) with (?:(un-verified|verified) )?text: (.+?(?:\\s+.+?)*)$")
    public void fill(String inputName, String pageName, String verify, String input) {
        input = contextCheck(input);
        WebElement inputElement = objectRepository.acquireElementFromPage(inputName, pageName);
        PolymorphicUtilities interactions = getInteractions(inputElement);
        interactions.fillInputElement(
                inputElement,
                inputName,
                pageName,
                input,
                !isPlatformElement(inputElement),
                true,
                Objects.equals(verify, "verified")
        );
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
        WebElement inputElement = objectRepository.acquireElementFromComponent(inputName, componentName, pageName);
        webInteractions.fillInputElement(inputElement, inputName, pageName, input, true, true, true);
    }

    /**
     * Fills in a form on the page with the given input values.
     *
     * @param pageName the name of the page containing the form
     * @param table    a DataTable containing the input values and corresponding element names
     * @throws WebDriverException if any input fields cannot be found or filled
     */
    @Given("^Fill form input on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void fillForm(String pageName, String driverType, DataTable table) {
        List<Bundle<WebElement, String, String>> inputBundles = objectRepository.acquireElementList(table.asMaps(), pageName);
        getInteractions(getType(driverType)).fillForm(inputBundles, pageName);
    }

    /**
     * Fills in a form within a component on the page with the given input values.
     *
     * @param componentName the name of the component containing the form
     * @param pageName      the name of the page containing the component
     * @param table         a DataTable containing the input values and corresponding element names
     * @throws WebDriverException if any input fields cannot be found or filled
     */
    @Given("Fill component form input of {} component on the {}")
    public void componentFillForm(String componentName, String pageName, DataTable table) {
        List<Bundle<WebElement, String, String>> inputBundles = objectRepository.acquireComponentElementList(
                table.asMaps(),
                componentName,
                pageName
        );
        webInteractions.fillForm(inputBundles, pageName);
    }

    /**
     * Fills in the specified input field within an iframe on the page with the given text.
     *
     * @param inputName  the name of the input field to fill in
     * @param iframeName the name of the iframe containing the input field
     * @param pageName   the name of the page containing the iframe and input field
     * @param inputText  the text to fill in the input field
     * @throws WebDriverException if the iframe or input field cannot be found or filled
     */
    @Given("Fill iFrame element {} of {} on the {} with text: {}")
    public void fillIframeInput(String inputName, String iframeName, String pageName, String inputText) {
        WebElement iframe = objectRepository.acquireElementFromPage(iframeName, pageName);
        WebElement element = objectRepository.acquireElementFromPage(inputName, pageName);
        webInteractions.fillIframeInput(iframe, element, inputName, pageName, inputText);
    }

    /**
     * Clicks on the specified i-frame element in the given page and i-frame.
     *
     * @param elementName the name of the i-frame element to be clicked
     * @param iframeName  the name of the i-frame containing the element
     * @param pageName    the name of the page containing the i-frame
     */
    @Given("Click i-frame element {} in {} on the {}")
    public void clickIframeElement(String elementName, String iframeName, String pageName) {
        WebElement iframe = objectRepository.acquireElementFromPage(iframeName, pageName);
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        webInteractions.clickIframeElement(iframe, element, elementName, iframeName, pageName);
    }

    /**
     * Fills the form inputs of the specified component in the given i-frame and page.
     *
     * @param iframeName    the name of the i-frame containing the component
     * @param componentName the name of the component to be filled
     * @param pageName      the name of the page containing the i-frame
     * @param table         the data table containing the input values for the form inputs
     */
    @Given("Fill iframe component form input {} of {} component on the {}")
    public void fillFormIframe(String iframeName, String componentName, String pageName, DataTable table) {
        List<Bundle<WebElement, String, String>> bundles = objectRepository.acquireComponentElementList(table.asMaps(), componentName, pageName);
        WebElement iFrame = objectRepository.acquireElementFromPage(iframeName, pageName);
        webInteractions.fillFormIframe(bundles, iFrame, iframeName, pageName);
    }

    /**
     * Fills the specified input of the specified component in the given list and page with the provided text.
     *
     * @param inputName     the name of the input to be filled
     * @param listName      the name of the list containing the component
     * @param componentName the name of the component containing the input
     * @param pageName      the name of the page containing the list and component
     * @param input         the text to be entered into the input
     */
    @Given("Fill listed component input {} of {} from {} list on the {} with text: {}")
    public void componentFillListedInput(String inputName, String listName, String componentName, String pageName, String input) {
        WebElement element = objectRepository.acquireListedComponentElement(inputName, componentName, listName, pageName);
        webInteractions.fillInputElement(element, inputName, pageName, input, true, true, true);
    }

    //Click listed component element {} of {} from {} list on the {}

    /**
     * Verifies that the text of the specified element on the specified page matches the expected text.
     *
     * @param elementName  the name of the element whose text is to be verified
     * @param pageName     the name of the page containing the element
     * @param expectedText the expected text of the element
     */
    @Given("^Verify the text of (\\w+) on the (\\w+) to be: (.+?(?:\\s+.+?)*)$")
    public void verifyText(String elementName, String pageName, String expectedText) {
        expectedText = contextCheck(expectedText);
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).centerElement(element, elementName, pageName);
        pageName = firstLetterDeCapped(pageName);
        getInteractions(element).verifyText(element, elementName, pageName, expectedText);
    }

    /**
     * Verifies that the text of the specified element on the specified page contains the expected text.
     *
     * @param elementName  the name of the element whose text is to be verified
     * @param pageName     the name of the page containing the element
     * @param expectedText the expected text of the element
     */
    @Given("^Verify the text of (\\w+) on the (\\w+) contains: (.+?(?:\\s+.+?)*)$")
    public void verifyContainsText(String elementName, String pageName, String expectedText) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).centerElement(element, elementName, pageName);
        pageName = firstLetterDeCapped(pageName);
        getInteractions(element).verifyContainsText(element, elementName, pageName, expectedText);
    }

    /**
     * Verifies that the text of the specified element in the specified component on the specified page matches the expected text.
     *
     * @param elementName   the name of the element whose text is to be verified
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     * @param expectedText  the expected text of the element
     */
    @Given("Verify text of the component element {} of {} on the {} to be: {}")
    public void componentVerifyText(String elementName, String componentName, String pageName, String expectedText) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.waitUntilVisible(element, elementName, pageName);
        webInteractions.centerElement(element, elementName, pageName);
        webInteractions.verifyElementText(element, expectedText);
    }

    /**
     * Verifies that the text contained within the specified component element on the specified page matches the expected text.
     *
     * @param elementName   The name or identifier of the component element to verify.
     * @param componentName The name or identifier of the component containing the element.
     * @param pageName      The name or identifier of the page where the component element is located.
     * @param expectedText  The expected text that the component element should contain.
     */
    @Given("Verify text of the component element {} of {} on the {} contains: {}")
    public void verifyComponentElementContainsText(String elementName, String componentName, String pageName, String expectedText) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.waitUntilVisible(element, elementName, pageName);
        webInteractions.centerElement(element, elementName, pageName);
        webInteractions.verifyElementContainsText(element, expectedText);
    }

    /**
     * Verifies that the text listed within the specified component elements on the specified page matches the expected text.
     *
     * @param listName      The name or identifier of the list containing the component elements to verify.
     * @param componentName The name or identifier of the component containing the elements.
     * @param pageName      The name or identifier of the page where the component elements are located.
     * @param table         A DataTable containing the expected text values for the component elements.
     */
    @Given("Verify text of component element list {} of {} on the {}") //TODO check
    public void verifyListedText(String listName, String componentName, String pageName, DataTable table) {
        List<Bundle<WebElement, String, String>> signForms = objectRepository.acquireElementList(table.asMaps(), pageName);
        webInteractions.verifyListedText(signForms, pageName);

        //List<Map<String, String>> forms = table.asMaps();
        //String elementName;
        //String expectedText;
        //for (Map<String, String> form : forms) {
        //  elementName = form.get("Input Element");
        //  expectedText = contextCheck(form.get("Input"));
        //  log.info();("Performing text verification for " +
        //          highlighted(BLUE, elementName) +
        //          highlighted(GRAY," on the ") +
        //          highlighted(BLUE, pageName) +
        //          highlighted(GRAY, " with the text: ") +
        //          highlighted(BLUE, expectedText)
        //  );
        //  pageName = firstLetterDeCapped(pageName);
        //  componentName = firstLetterDeCapped(componentName);
        //  List<WebElement> elements = getElementsFromComponent(listName, componentName, pageName);
        //  WebElement element = acquireNamedElementAmongst(elements, elementName);
        //  Assert.assertEquals("The " + element.getText() + " does not contain text '",expectedText,element.getText());
        //  log.success();("Text of the element " + element.getText() + " was verified!");
        //}
    }

    /**
     * Verifies the absence of an element with the specified text from a list of components on the specified page.
     *
     * @param elementName       The name or identifier of the element to verify its absence.
     * @param elementText       The text of the element to be absent.
     * @param componentListName The name or identifier of the list of components to search within.
     * @param pageName          The name or identifier of the page where the list of components is located.
     */
    @Given("Verify absence element {} with text {} from list of components {} on the {}")
    public void verifyAbsenceOfElementWithTextFromListOfComponents(String elementName, String elementText, String componentListName, String pageName) {
        componentListName = firstLetterDeCapped(componentListName);
        pageName = firstLetterDeCapped(pageName);
        Assert.assertFalse(
                "The elements from " + componentListName + " contain element " + elementName +
                        " with text '" + elementText + "' ",
                objectRepository.listedComponentContainsElementText(elementName, elementText, componentListName, pageName)
        );
        log.success("The elements from " + componentListName + " don´t contain " + elementName +
                " with text '" + elementText + "'!");
    }

    /**
     * Verifies that the specified element is absent on the specified page.
     *
     * @param elementName the name of the element to be verified
     * @param pageName    the name of the page containing the element
     */
    @Given("^Verify absence of element (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void verifyAbsence(String elementName, String pageName, String driverType) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(getType(driverType)).verifyElementState(element, elementName, pageName, ElementState.absent);
    }

    /**
     * Verifies that the specified element is present on the specified page.
     *
     * @param elementName the name of the element to be verified
     * @param pageName    the name of the page containing the element
     */
    @Given("^Verify presence of element (\\w+) on the (\\w+)$")
    public void verifyPresence(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).verifyElementState(element, elementName, pageName, ElementState.displayed);
    }

    /**
     * Verifies that the specified element is present on the specified page.
     *
     * @param componentName the name of the element to be verified
     * @param pageName      the name of the page containing the element
     */
    @Given("^Verify presence of component (\\w+) on the (\\w+)$")
    public void verifyComponentPresence(String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(componentName, pageName);
        getInteractions(selenium).verifyElementState(element, componentName, pageName, ElementState.displayed);
    }

    /**
     * Verifies that the specified element in the specified component is present on the specified page.
     *
     * @param elementName   the name of the element to be verified
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     */
    @Given("Verify presence of the component element {} of {} on the {}")
    public void componentVerifyPresence(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.verifyElementState(element, elementName, pageName, ElementState.displayed);
    }

    /**
     * Verifies the presence of text within the specified elements on the specified page.
     *
     * @param pageName The name or identifier of the page containing the elements to verify.
     * @param table    A DataTable containing the elements and their expected text values.
     */
    @Given("Checking the presence of the element text on the {}") //TODO check
    public void verifyPresenceText(String pageName, DataTable table) {
        List<Bundle<WebElement, String, String>> elements = objectRepository.acquireElementList(table.asMaps(), pageName);
        for (Bundle<WebElement, String, String> element : elements) {
            WebElement targetElement = objectRepository.acquireElementFromPage(element.beta(), pageName);
            webInteractions.verifyElementState(targetElement, element.beta(), pageName, ElementState.enabled);

        }

        //String elementText;
        //List<Map<String, String>> signForms = table.asMaps();
        //List<Map<String, String>> signForms = table.asMaps();
        //for (Map<String, String> form : signForms) {
        //  elementText = contextCheck(form.get("Input"));
        //  log.info();("Performing text verification for " +
        //          highlighted(BLUE, elementText) +
        //          highlighted(GRAY, " on the ") +
        //          highlighted(BLUE, pageName)
        //  );
        //
        //  WebElement element = getElementContainingText(elementText);
        //  verifyElementState(element, ElementState.enabled);
        //  log.success();("Presence of the element text " + elementText + " was verified!");
        //}
    }

    /**
     * Closes the current browser window.
     */
    @Given("Close the browser")
    public void closeBrowser() {
        webInteractions.driver.quit();
        initialiseBrowser = isWebUI;
    }

    /**
     * This method verifies if the given element on the given page is in the expected state.
     *
     * @param elementName   The name of the element to be verified.
     * @param pageName      The name of the page on which the element is located.
     * @param expectedState The expected state of the element.
     */
    @Given("^Verify that element (\\w+) on the (\\w+) is in (\\w+) state$")
    public void verifyState(String elementName, String pageName, ElementState expectedState) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).verifyElementState(element, elementName, pageName, expectedState);
    }

    /**
     * This method verifies if the given component element on the given page is in the expected state.
     *
     * @param elementName   The name of the component element to be verified.
     * @param componentName The name of the component that contains the element.
     * @param pageName      The name of the page on which the component is located.
     * @param expectedState The expected state of the element.
     */

    @Given("Verify that component element {} of {} on the {} is in {} state")
    public void verifyState(String elementName, String componentName, String pageName, ElementState expectedState) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.verifyElementState(element, elementName, pageName, expectedState);
    }

    /**
     * This method verifies if the given component element on the given page is in the expected state, if it is present.
     *
     * @param elementName   The name of the component element to be verified.
     * @param componentName The name of the component that contains the element.
     * @param pageName      The name of the page on which the component is located.
     * @param expectedState The expected state of the element.
     */
    @Given("If present, verify that component element {} of {} on the {} is in {} state")
    public void verifyIfPresentElement(String elementName, String componentName, String pageName, ElementState expectedState) {
        try {
            WebElement element = objectRepository
                    .acquireElementFromComponent(elementName, componentName, pageName);
            webInteractions.verifyElementState(element, elementName, pageName, expectedState);
        } catch (WebDriverException ignored) {
            log.warning("The " + elementName + " was not present");
        }
    }

    /**
     * This method waits until the given element is absent from the given page.
     *
     * @param elementName The name of the element to wait for absence.
     * @param pageName    The name of the page on which the element is located.
     */
    @Given("^Wait for absence of element (\\w+) on the (\\w+)(?: using (Mobile|Web) driver)?$")
    public void waitUntilAbsence(String elementName, String pageName, DriverFactory.DriverType driverType) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(driverType).waitUntilAbsence(element, elementName, pageName);
    }

    /**
     * Waits until the specified component element is absent from the specified page.
     *
     * @param elementName   The name or identifier of the component element to wait for its absence.
     * @param componentName The name or identifier of the component containing the element.
     * @param pageName      The name or identifier of the page where the component element is located.
     */
    @Given("Wait for absence of component element {} of {} on the {}")
    public void componentWaitUntilAbsence(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.waitUntilAbsence(element, elementName, pageName);
    }

    /**
     * Wait for an element on a specific page to be visible.
     *
     * @param elementName the name of the element to be verified
     * @param pageName    the name of the page containing the element
     */
    @Given("^Wait for element (\\w+) on the (\\w+) to be visible$")
    public void waitUntilVisible(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).waitUntilVisible(element, elementName, pageName);
    }

    /**
     * Wait for a component element on a specific page to be visible.
     *
     * @param elementName   the name of the element to be verified
     * @param componentName the name of the component containing the element
     * @param pageName      the name of the page containing the component
     */
    @Given("Wait for component element {} of {} on the {} to be visible")
    public void componentWaitUntilVisible(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.waitUntilVisible(element, elementName, pageName);
    }

    /**
     * Waits until all elements with the specified name are visible within all components in the specified component list on the specified page.
     *
     * @param elementName       The name or identifier of the element to wait for its visibility within components.
     * @param componentListName The name or identifier of the component list containing the components.
     * @param pageName          The name or identifier of the page where the component list is located.
     */
    @Given("Wait for listed component element {} of {} on the {} to be visible")
    public void componentListWaitUntilVisible(String elementName, String componentListName, String pageName) {
        pageName = firstLetterDeCapped(pageName);
        componentListName = firstLetterDeCapped(componentListName);
        elementName = contextCheck(elementName);
        List<WebComponent> componentList = pageObjectReflections.getComponentsFromPage(componentListName, pageName);
        List<WebElement> webElements = new ArrayList<>();
        for (WebComponent component : componentList) {
            WebElement element = pageObjectReflections.getElementFromComponent(elementName, component);
            webElements.add(element);
        }
        String finalElementName = elementName;
        String finalPageName = pageName;
        webElements.forEach(webElement ->
                webInteractions.waitUntilVisible(webElement, finalElementName, finalPageName));
    }

    /**
     * Waits until the specified target element within the selected component contains the expected attribute state on the specified page.
     *
     * @param elementFieldName       The name or identifier of the element field within the component.
     * @param elementText            The text of the element to be matched within the component list.
     * @param componentListName      The name or identifier of the component list containing the component.
     * @param pageName               The name or identifier of the page where the component list is located.
     * @param targetElementFieldName The name or identifier of the target element within the component.
     * @param expectedState          The expected state of the target element's attribute.
     */
    @Given("Select component by {} named {} from {} component list on the {} and wait for {} element to be {}")
    public void waitSelectedComponentContainsAttribute(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String targetElementFieldName,
            ElementState expectedState) {
        WebComponent component = objectRepository.acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        WebElement button = pageObjectReflections.getElementFromComponent(targetElementFieldName, component);
        webInteractions.verifyElementState(button, targetElementFieldName, pageName, expectedState);
    }

    /**
     * Wait until an element on a specific page has a specific value for its attribute.
     *
     * @param elementName    the name of the element to be verified
     * @param pageName       the name of the page containing the element
     * @param attributeValue the expected value of the attribute
     * @param attributeName  the name of the attribute to be verified
     */
    @Given("^Wait until element (\\w+) on the (\\w+) has (.+?(?:\\s+.+?)*) value for its (\\w+) attribute$")
    public void waitUntilElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = contextCheck(attributeValue);
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        try {
            getInteractions(element).waitUntilElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
        } catch (WebDriverException ignored) {
        }
    }

    /**
     * Wait until a component element on a specific page has a specific value for its attribute.
     *
     * @param elementName    the name of the element to be verified
     * @param componentName  the name of the component containing the element
     * @param pageName       the name of the page containing the component
     * @param attributeValue the expected value of the attribute
     * @param attributeName  the name of the attribute to be verified
     */
    @Given("Wait until component element {} of {} on the {} has {} value for its {} attribute")
    public void componentWaitUntilElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = contextCheck(attributeValue);
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        try {
            webInteractions.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
        } catch (WebDriverException ignored) {
        }
    }

    /**
     * Verify that an element on a specific page has a specific value for its attribute.
     *
     * @param elementName    the name of the element to be verified
     * @param pageName       the name of the page containing the element
     * @param attributeValue the expected value of the attribute
     * @param attributeName  the name of the attribute to be verified
     */
    @Given("^Verify that element (\\w+) on the (\\w+) has (.+?(?:\\s+.+?)*) value for its (.+?(?:\\s+.+?)*) attribute$")
    public void verifyElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    /**
     * Verify that an attribute of element on a specific page contains a specific value.
     *
     * @param attributeName the name of the attribute to be verified
     * @param elementName   the name of the element to be verified
     * @param pageName      the name of the page containing the element
     * @param value         the expected part of value of the attribute
     */
    @Given("^Verify that (\\w+) attribute of element (\\w+) on the (\\w+) contains (.+?(?:\\s+.+?)*) value")
    public void verifyElementAttributeContainsValue(
            String attributeName,
            String elementName,
            String pageName,
            String value
    ) {
        value = contextCheck(value);
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        webInteractions.verifyElementAttributeContainsValue(element, attributeName, elementName, pageName, value);
        log.info("-> " + markup(BLUE, value));
    }

    /**
     * Verify the CSS attribute of an element on a specific page.
     *
     * @param attributeName  the name of the CSS attribute to be verified
     * @param elementName    the name of the element to be verified
     * @param pageName       the name of the page containing the element
     * @param attributeValue the expected value of the CSS attribute
     */
    @Given("Verify {} css attribute of element {} on the {} is {}")
    public void verifyElementColor(
            String attributeName,
            String elementName,
            String pageName,
            String attributeValue) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        webInteractions.verifyElementColor(element, attributeName, elementName, pageName, attributeValue);
    }

    /**
     * Verify the CSS attribute of an element on a specific page.
     *
     * @param attributeName  the name of the CSS attribute to be verified
     * @param elementName    the name of the element to be verified
     * @param pageName       the name of the page containing the element
     * @param attributeValue the expected value of the CSS attribute
     */
    @Given("Verify {} css attribute of element {} from {} component on {} is {}")
    public void verifyElementColor(
            String attributeName,
            String elementName,
            String componentName,
            String pageName,
            String attributeValue) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.verifyElementColor(element, attributeName, elementName, pageName, attributeValue);
    }

    /**
     * Verify that a component element on a specific page has a specific value for its attribute.
     *
     * @param elementName    the name of the element to be verified
     * @param componentName  the name of the component containing the element
     * @param pageName       the name of the page containing the component
     * @param attributeValue the expected value of the attribute
     * @param attributeName  the name of the attribute to be verified
     */
    @Given("Verify that component element {} of {} on the {} has {} value for its {} attribute")
    public void ComponentVerifyElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
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
    @Given("Select component by {} named {} from {} component list on the {} and verify that it has {} value for its {} attribute")
    public void verifySelectedComponentContainsAttribute(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebComponent component = objectRepository.acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        webInteractions.verifyElementContainsAttribute(component, elementText, pageName, attributeName, attributeValue);
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
        WebElement element = objectRepository.acquireExactNamedListedComponentElement(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        webInteractions.verifyElementContainsAttribute(element, elementText, pageName, attributeName, attributeValue);
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
    public void selectComponentByText(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String targetElementFieldName) {
        elementText = contextCheck(elementText);
        WebComponent component = objectRepository.acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        WebElement button = pageObjectReflections.getElementFromComponent(targetElementFieldName, component);
        webInteractions.clickElement(button, targetElementFieldName, pageName);
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
    @Given("Perform JS click on {} element by selecting component {} named {} from {} component list on the {}")
    public void selectComponentByTextJSClick(
            String targetElementFieldName,
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName) {
        elementText = contextCheck(elementText);
        WebComponent component = objectRepository.acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        WebElement button = pageObjectReflections.getElementFromComponent(targetElementFieldName, component);
        webInteractions.performJSClick(button, targetElementFieldName, pageName);
    }

    /**
     * Given the attribute and its value of a web element inside a WebComponent,
     * this method selects the component and then clicks on a specified element within it.
     *
     * @param elementFieldName       The field name of the desired element in the WebComponent.
     * @param attributeName          The attribute name that the desired element should contain.
     * @param attributeValue         The attribute value that the desired element should contain.
     * @param componentListName      The name of the list where the component can be found.
     * @param pageName               The name of the page where the component is located.
     * @param targetElementFieldName The field name of the element within the component that should be clicked.
     *                               <p>
     *                               The function first retrieves the WebComponent from the specified list on the given page,
     *                               based on the provided field name and text. Then it identifies the target element within the component using
     *                               the provided attributeName and attributeValue. Finally, it performs a click interaction on the identified target element.
     *                               <p>
     *                               The method assumes that the necessary components and elements exist and are accessible.
     */

    @Given("Select listed component by {} element attribute named {} value {} from {} component list on the {} and click the {} element")
    public void selectElementByComponentContainsAttribute(
            String elementFieldName,
            String attributeName,
            String attributeValue,
            String componentListName,
            String pageName,
            String targetElementFieldName) {
        List<WebElement> components = pageObjectReflections.getComponentsFromPage(componentListName, pageName);
        WebElement component = ElementAcquisition.acquireComponentByElementAttributeAmongst(components, attributeName, attributeValue, elementFieldName);
        WebElement button = pageObjectReflections.getElementFromComponent(targetElementFieldName, component);
        webInteractions.clickElement(button, targetElementFieldName, pageName);
    }

    /**
     * Verify that a listed element from a specific page has a specific value for its attribute.
     *
     * @param elementName    the name of the element to be verified
     * @param listName       the name of the list containing the element
     * @param pageName       the name of the page containing the list
     * @param attributeValue the expected value of the attribute
     * @param attributeName  the name of the attribute to be verified
     */
    @Given("^Verify that element (\\w+) from (\\w+) list on the (\\w+) has (.+?(?:\\s+.+?)*) value for its (\\w+) attribute$")
    public void verifyListedElementContainsAttribute(
            String elementName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName) {
        WebElement element = objectRepository.acquireListedElementFromPage(elementName, listName, pageName);
        getInteractions(element).verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    /**
     * Verifies if an element in a list contains a specified text.
     *
     * @param elementName  the name of the element to be verified.
     * @param listName     the name of the list containing the element.
     * @param pageName     the name of the page containing the list.
     * @param expectedText the expected text to be verified in the element.
     */
    @Given("^Select listed element containing partial text (.+?(?:\\s+.+?)*) from the (\\w+) on the (\\w+) and verify its text contains (.+?(?:\\s+.+?)*)$")
    public void verifyListedElementContainsText(
            String elementName,
            String listName,
            String pageName,
            String expectedText) {
        WebElement element = objectRepository.acquireListedElementFromPage(elementName, listName, pageName);
        getInteractions(element).verifyContainsText(element, elementName, pageName, expectedText);
    }

    /**
     * Verifies that each listed element within the specified list on the specified page contains the expected text.
     *
     * @param listName The name or identifier of the list containing the elements to verify.
     * @param pageName The name or identifier of the page where the list is located.
     * @param table    A DataTable containing the expected text values for the listed elements.
     */
    @Given("Verify text of listed element from the {} on the {}") //TODO: fix this step
    public void verifyListedElementContainsText(String listName, String pageName, DataTable table) {
        List<Map<String, String>> signForms = table.asMaps();
        String elementText;
        for (Map<String, String> form : signForms) {
            elementText = form.get("Element Text");

            WebElement element = objectRepository.acquireListedElementFromPage(elementText, listName, pageName);
            log.info("Performing text verification for element with text '" +
                    highlighted(BLUE, elementText) +
                    highlighted(GRAY, "' on the ") +
                    highlighted(BLUE, pageName)
            );
            Assert.assertTrue(
                    "The " + elementText + " does not contain text '" + elementText + "' ",
                    element.getText().contains(elementText)
            );
            log.success("Element is verified to contain '" + elementText + "' text!");
        }
    }

    /**
     * Verifies if an element contains a specified text.
     *
     * @param elementName  the name of the element to be verified.
     * @param pageName     the name of the page containing the list.
     * @param expectedText the expected text to be verified in the element.
     */
    @Given("Verify text of (.+?(?:\\s+.+?)*) element on the (.+?(?:\\s+.+?)*) contains (.+?(?:\\s+.+?)*)$")
    public void verifyElementContainsText(
            String elementName,
            String pageName,
            String expectedText) {
        expectedText = contextCheck(expectedText);
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).centerElement(element, elementName, pageName);
        getInteractions(element).verifyContainsText(element, elementName, pageName, expectedText);
    }

    /**
     * Verifies if an element contains a specified text from another element.
     *
     * @param firstElementName  the name of the main element going to be verified if contains a text.
     * @param pageName          the name of the page containing the list.
     * @param secondElementName the name of the element going to be verified if includes in the main element.
     */
    @Given("Perform text verification for (.+?(?:\\s+.+?)*) element on the (.+?(?:\\s+.+?)*) contains text of (.+?(?:\\s+.+?)*)$")
    public void verifyElementsTextMatch(
            String firstElementName,
            String pageName,
            String secondElementName) {
        WebElement firstElement = objectRepository.acquireElementFromPage(firstElementName, pageName);
        WebElement secondElement = objectRepository.acquireElementFromPage(secondElementName, pageName);
        getInteractions(firstElement).centerElement(secondElement, secondElementName, pageName);
        getInteractions(firstElement).verifyContainsText(firstElement, firstElementName, pageName, secondElement.getText());
    }

    /**
     * Verifies if a component element in a list contains a specified text.
     *
     * @param elementName   the name of the element to be verified.
     * @param listName      the name of the list containing the element.
     * @param componentName the name of the component containing the element.
     * @param pageName      the name of the page containing the list.
     * @param expectedText  the expected text to be verified in the element.
     */
    @Given("Verify text of listed component element {} from the {} of {} on the {} is equal to {}")
    public void verifyListedComponentElementContainsText(
            String elementName,
            String listName,
            String componentName,
            String pageName,
            String expectedText) {
        WebElement element = objectRepository.acquireListedElementFromComponent(elementName, componentName, listName, pageName);
        webInteractions.verifyContainsText(element, elementName, pageName, expectedText);
    }

    /**
     * Verifies that each listed element within the specified component on the specified page contains the expected text.
     *
     * @param listName      The name or identifier of the list containing the component elements to verify.
     * @param componentName The name or identifier of the component containing the elements.
     * @param pageName      The name or identifier of the page where the component and list are located.
     * @param table         A DataTable containing the expected text values for the listed component elements.
     */
    @Given("Verify text of listed component element from the {} of {} on the {}") //TODO check
    public void verifyListedComponentElementContainsText(String listName, String componentName, String pageName, DataTable table) {
        List<Map<String, String>> signForms = table.asMaps();
        String elementText;
        for (Map<String, String> form : signForms) {
            elementText = form.get("Element Text");

            WebElement element = objectRepository.acquireListedElementFromComponent(elementText, componentName, listName, pageName);
            log.info("Performing text verification for element with text '" +
                    highlighted(BLUE, elementText) +
                    highlighted(GRAY, "' on the ") +
                    highlighted(BLUE, pageName)
            );
            Assert.assertTrue(
                    "The " + elementText + " does not contain text '" + elementText + "' ",
                    element.getText().contains(elementText)
            );
            log.success("Element is verified to contain '" + elementText + "' text!");
        }
    }

    /**
     * Verifies that each listed element within the specified list on the specified page contains the expected text.
     *
     * @param listName     The name or identifier of the list containing the elements to verify.
     * @param pageName     The name or identifier of the page where the list is located.
     * @param expectedText The expected text that each listed element should contain.
     */
    @Given("Perform text verification for listed elements of {} list on the {} contains {}")
    public void verifyListedElementsContainsText(
            String listName,
            String pageName,
            String expectedText) {
        pageName = firstLetterDeCapped(pageName);
        log.info("Performing text verification for elements of the '" +
                highlighted(BLUE, listName) +
                highlighted(GRAY, "' list on the ") +
                highlighted(BLUE, pageName)
        );
        List<WebElement> elementList = pageObjectReflections.getElementsFromPage(listName, pageName);
        for (WebElement element : elementList) {
            if (element.getText().contains(expectedText)) log.success("Element contains '" + expectedText + "' text!");
            else
                log.warning("Element not contains '" + expectedText + "' text! -> " + markup(PURPLE, element.getText()));
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
        WebElement element = objectRepository.acquireListedElementFromComponent(elementText, componentName, listName, pageName);
        webInteractions.verifyPresence(element, elementText, pageName);
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
        WebElement element = objectRepository.acquireListedElementFromComponent(elementName, componentName, listName, pageName);
        webInteractions.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    /**
     * Verifies if the current URL contains a specified text.
     *
     * @param text the text to be verified in the current URL.
     */
    @Given("Verify the page is redirecting to the page {}")
    @Given("Verify the url contains with the text {}")
    public void verifyTextUrl(String text) {
        webInteractions.verifyUrlContains(text);
        log.success("The url contains '" + text + "'");
    }

    /**
     * Verifies that the actual value matches the expected value.
     *
     * <p>
     * This method compares the expected value with the actual value and asserts their equality.
     * If the values do not match, an AssertionError is thrown.
     * </p>
     *
     * @param expectedValue The expected value to be compared with the actual value.
     * @param actualValue   The actual value to be compared with the expected value.
     */
    @Given("Assert that value of {} is equal to {}")
    public void verifyText(String expectedValue, String actualValue) {
        log.info("Checking values...");
        expectedValue = contextCheck(expectedValue).replaceAll(",", "");
        actualValue = contextCheck(actualValue).replaceAll(",", "");
        Assert.assertEquals("Values not match!", expectedValue, actualValue);
        log.success("Values verified as: " + actualValue);
    }

    /**
     * Verifies that the actual value matches the expected value.
     *
     * <p>
     * This method compares the expected value with the actual value and asserts their equality.
     * If the values do not match, an AssertionError is thrown.
     * </p>
     *
     * @param expectedValue The expected value to be compared with the actual value.
     * @param actualValue   The actual value to be compared with the expected value.
     */
    @Given("Assert that value of {} contains {}")
    public void assertContains(String expectedValue, String actualValue) {
        log.info("Checking values...");
        expectedValue = contextCheck(expectedValue).replaceAll(",", "");
        actualValue = contextCheck(actualValue).replaceAll(",", "");
        if (actualValue.contains(expectedValue)) log.success("Values verified as: " + actualValue);
        else throw new PickleibVerificationException("'" + actualValue + "' not contains '" + expectedValue + "'");
    }

    @Given("Assert that value of {} is not equal to {}")
    public void verifyNoText(String expectedValue, String actualValue) {
        log.info("Checking values...");
        expectedValue = contextCheck(expectedValue).replaceAll(",", "");
        actualValue = contextCheck(actualValue).replaceAll(",", "");
        Assert.assertNotEquals("Values should not match!", expectedValue, actualValue);
        log.success("Values verified as: " + actualValue);
    }

    @Given("Assert the value of {} attribute for {} element on {} is equal to {}")
    public void assertAttribute(String attributeName, String elementName, String pageName, String actualValue) {
        log.info("Acquiring the" + attributeName + " value...");
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        String value = element.getAttribute(attributeName);
        Assert.assertEquals("Values not match!", value, actualValue);
        log.success("Values verified as: " + actualValue);
    }

    @Given("Assert the value of {} attribute for {} element from {} component on {} is equal to {}")
    public void componentAssertAttribute(String attributeName, String elementName, String componentName, String pageName, String actualValue) {
        log.info("Acquiring the" + attributeName + " value...");
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        String expectedValue = element.getAttribute(attributeName);
        actualValue = contextCheck(actualValue);
        Assert.assertEquals("Values not match!", expectedValue, actualValue);
        log.success("Values verified as: " + actualValue);
    }

    /**
     * Updates the context with a new key-value pair.
     *
     * @param key   the key for the new context data.
     * @param value the value for the new context data.
     */
    @Given("Update context {} -> {}")
    public void updateContext(String key, String value) {
        log.info("Updating context " + markup(BLUE, key) + " to " + markup(BLUE, value));
        ContextStore.put(key, contextCheck(value));
    }

    /**
     * Clears the input field of a page.
     *
     * @param elementName the name of the input field to be cleared.
     * @param pageName    the name of the page containing the component.
     */
    @Given("^Clear input field (\\w+) on the (\\w+)$")
    public void clearInputField(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        element.clear(); // TODO Inner clear() method may be needed
    }

    /**
     * Clears the input field of a component.
     *
     * @param elementName   the name of the input field to be cleared.
     * @param componentName the name of the component containing the input field.
     * @param pageName      the name of the page containing the component.
     */
    @Given("Clear component input field {} from {} component on the {}")
    public void componentClearInputField(String elementName, String componentName, String pageName) {
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
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
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).pressKey(element, elementName, pageName, key);
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
        WebElement element = objectRepository.acquireElementFromComponent(elementName, componentName, pageName);
        webInteractions.pressKey(element, elementName, pageName, key);
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
     * @param eventName    the name of the event to be listened to.
     * @param objectScript the script used to obtain the object from the event.
     */
    @Given("Listen to {} event & print {} object")
    public void listenGetAndPrintObjectStep(String eventName, String objectScript) {
        String listenerScript = "_ddm.listen(" + eventName + ");";
        webInteractions.listenGetAndPrintObject(listenerScript, eventName, objectScript);
    }

    /**
     * Listens for an event, retrieves the value of a specified node, and verifies it against an expected value.
     *
     * <p>
     * This method listens for the specified event using a listener script generated based on the event name.
     * It then retrieves the value of the specified node from the event data and verifies it against the expected value.
     * </p>
     *
     * @param eventName     The name of the event to listen for.
     * @param nodeSource    The source node whose value will be verified.
     * @param expectedValue The expected value to compare with the retrieved value.
     */
    @Given("Listen to {} event & verify value of {} node is {}")
    public void listenGetAndVerifyObjectStep(String eventName, String nodeSource, String expectedValue) {
        log.info("Verifying value of '" + nodeSource + "' node");
        String listenerScript = "_ddm.listen(" + eventName + ");";
        webInteractions.listenGetAndVerifyObject(listenerScript, eventName, nodeSource, expectedValue);
    }

    /**
     * Fills the input field of a given component with a specified file.
     *
     * @param inputName     the name of the input field to be filled.
     * @param componentName the name of the component containing the input field.
     * @param pageName      the name of the page containing the component.
     * @param path          the file path of the file to be uploaded.
     */
    @Given("Upload file on component input {} of {} component on the {} with file: {}")
    public void fillInputWithFile(String inputName, String componentName, String pageName, String path) {
        WebElement inputElement = objectRepository.acquireElementFromComponent(inputName, componentName, pageName);
        webInteractions.fillInputWithFile(inputElement, inputName, pageName, path);
    }

    /**
     * Listens for a specified event and verifies the values of the specified nodes in the resulting object.
     *
     * @param eventName The name of the event to listen for.
     * @param nodeTable A DataTable containing the names of the nodes to verify in the resulting object.
     */
    @Given("Listen to {} event & verify values of the following nodes")
    public void listenGetAndVerifyObjectStep(String eventName, DataTable nodeTable) {
        String listenerScript = "_ddm.listen(" + eventName + ");";
        webInteractions.listenGetAndVerifyObject(listenerScript, eventName, nodeTable.asMaps());
    }

    /**
     * Performs text replacement on a specified attribute value by replacing a specified split value with a new value.
     *
     * @param attributeText The attribute value to be modified.
     * @param splitValue    The value to be replaced within the attribute value.
     * @param attributeName The name of the attribute being modified.
     */
    @Given("Perform text replacement on {} context by replacing {} value in {}")
    public void replaceAttributeValue(String attributeText, String splitValue, String attributeName) {
        attributeText = contextCheck(attributeText);
        log.info("Acquiring " + highlighted(BLUE, attributeText));
        log.info("Removing -> " + highlighted(BLUE, splitValue));
        ContextStore.put(attributeName, attributeText.replace(splitValue, ""));
        log.info("Updated value -> " + highlighted(GREEN, ContextStore.get(attributeName)));
    }

    /**
     * Selects a component from a specified component list in a page object, based on the text of a child element, and performs interactions with the second child element based on provided specifications.
     *
     * @param componentListName The name of the component list in the page object.
     * @param pageName          The name of the page object.
     * @param table             A DataTable containing the specifications for each second child element to be interacted with, including the selector text, selector element name, target element name, and interaction type.
     */
    @Given("Select component amongst {} list by child element and interact with a second child on the {}")
    public void selectComponentByChildAndInteractWithSecondChild(
            String componentListName,
            String pageName,
            DataTable table
    ) {
        List<Bundle<String, WebElement, Map<String, String>>> bundles =
                objectRepository.selectChildElementsFromComponentsBySecondChildText(
                        table.asMaps(),
                        componentListName,
                        pageName
                );
        webInteractions.bundleInteraction(bundles, pageName);
    }

    @Given("Select component by {} named {} from {} component list on the {} and verify {} element contains {} text")
    public void verifySelectedComponentContainsText(
            String elementFieldName,
            String elementText,
            String componentListName,
            String pageName,
            String targetElementFieldName,
            String expectedText) {
        WebComponent component = objectRepository.acquireExactNamedListedComponent(
                elementFieldName,
                elementText,
                componentListName,
                pageName
        );
        WebElement element = pageObjectReflections.getElementFromComponent(targetElementFieldName, component);
        webInteractions.verifyElementContainsText(element, expectedText);
    }

    /**
     * Performs interactions with elements on a specified page object, based on the provided specifications.
     *
     * @param pageName       The name of the page object.
     * @param specifications A DataTable containing the specifications for each element to be interacted with, including the element name and interaction type.
     */
    @Given("^Interact with element on the (\\w+) of (Mobile|Web) driver?$")
    public void pageElementInteraction(String pageName, String driverType, DataTable specifications) {
        List<Bundle<String, WebElement, Map<String, String>>> bundles = objectRepository.acquireElementBundlesFromPage(
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
            DataTable specifications //TODO: fix this step and its logs
    ) {
        List<Bundle<String, WebElement, Map<String, String>>> bundles = objectRepository.acquireElementBundlesFromComponent(
                componentFieldName,
                pageName,
                specifications.asMaps()
        );
        webInteractions.bundleInteraction(bundles, pageName);
    }

    /**
     * Save context value to another context key to save it before it will be overwritten.
     */
    @Given("Save context value from {} context key to {}")
    public void saveContextValueFromOneContextKeyToAnother(String contextKey, String newContextKey) {
        Object value = ContextStore.get(contextKey);
        ContextStore.put(newContextKey, value);
        log.info("Value of " + markup(BLUE, contextKey) + markup(GREEN, " was saved to ") + markup(BLUE, newContextKey));
    }

    @Given("Execute script {string} on element with text {string}")
    public void executeScript(String script, String elementText) {
        WebElement element = webInteractions.getElementContainingText(elementText);
        webInteractions.executeScript(script, element);
    }
}
