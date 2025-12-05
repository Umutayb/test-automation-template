package steps;
import collections.Bundle;
import com.google.common.collect.ImmutableMap;
import context.ContextStore;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import common.ObjectRepository;
import org.junit.Assert;
import org.openqa.selenium.*;
import pickleib.driver.DriverFactory;
import pickleib.enums.Direction;
import pickleib.enums.ElementState;
import pickleib.enums.Navigation;
import pickleib.exceptions.PickleibVerificationException;
import pickleib.mobile.driver.PickleibAppiumDriver;
import pickleib.mobile.interactions.PlatformInteractions;
import pickleib.utilities.interfaces.PolymorphicUtilities;
import pickleib.utilities.steps.PageObjectStepUtilities;
import pickleib.web.driver.PickleibWebDriver;
import pickleib.web.interactions.WebInteractions;
import utils.*;
import java.util.*;

import static pickleib.driver.DriverFactory.DriverType.*;
import static pickleib.utilities.platform.PlatformUtilities.isPlatformElement;
import static steps.Hooks.initialiseAppiumDriver;
import static utils.StringUtilities.*;
import static utils.StringUtilities.Color.*;
import static utils.StringUtilities.markup;


public class CommonSteps extends PageObjectStepUtilities<ObjectRepository> {

    WebInteractions webInteractions;
    PlatformInteractions platformInteractions;

    public CommonSteps() {
        super(ObjectRepository.class, initialiseAppiumDriver, Hooks.initialiseBrowser);
        if (initialiseAppiumDriver)
            platformInteractions = new PlatformInteractions();
        if (Hooks.initialiseBrowser)
            webInteractions = new WebInteractions();
    }

    @Override
    public PolymorphicUtilities getInteractions(DriverFactory.DriverType driverType) {
        if (!StringUtilities.isBlank(driverType))
            switch (driverType) {
                case selenium -> {
                    return webInteractions;
                }
                case appium-> {
                    return platformInteractions;
                }
            }
        else return getInteractions(defaultPlatform);
        return null;
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
    @Given("^Set default platform as (appium|selenium)$")
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
        webInteractions.getUrl(url);
    }


    /**
     * Navigates to the specified environment page.
     *
     * @param environment The environment to navigate to (acceptance, test, or dev).
     */
    @Given("^Navigate to the (acceptance|test|dev) page$")
    public void navigateToTargetEnv(ObjectRepository.Environment environment) {
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
     * Switches to next active window.
     *
     */
    @Given("Switch to the next active window")
    public void switchToNextActiveWindow() {
        PickleibAppiumDriver.get().switchTo().window(PickleibAppiumDriver.get().getWindowHandles().stream().findAny().orElseGet(null));
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
     * Clicks or taps a specified element on a given page if it is enabled.
     * <p>
     * This method retrieves an element by its name from a page in the object repository,
     * checks if it is enabled, and performs a click action if the condition is met. If the
     * element is not present or an error occurs, a warning is logged and the exception is ignored.
     * </p>
     *
     * @param elementName The name of the element to interact with (e.g., "button", "link").
     * @param pageName    The name of the page containing the element (e.g., "loginPage").
     */
    @Given("^If enabled, (?:click|tap) the (\\w+) on the (\\w+)$")
    public void clickIfEnabled(String elementName, String pageName) {
        try {
            WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
            if (getInteractions(element).elementIs(element, elementName, pageName, ElementState.enabled))
                getInteractions(element).clickElement(element, elementName, pageName);
        } catch (WebDriverException ignored) {
            log.warning("The " + elementName + " was not present");
        }
    }

    /**
     * Waits for the specified duration in seconds.
     *
     * @param duration   the duration to wait in seconds
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
     * Centers the specified element on the page in the viewport.
     *
     * @param elementName the name of the element to center
     * @param pageName    the name of the page containing the element
     */
    @Given("Center the {} on the {}")
    public void center(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        webInteractions.centerElement(element, elementName, pageName);
    }

    /**
     * Centers the specified element on the page in the viewport.
     *
     * @param elementName the name of the element to center
     * @param elementListName the name of the element list that includes the element
     * @param pageName    the name of the page containing the element
     */
    @Given("Center element named {} on the {} from {}")
    public void centerListedElement(String elementName, String elementListName, String pageName) {
        elementName = contextCheck(elementName);
        WebElement element = objectRepository.acquireListedElementFromPage(elementName, elementListName, pageName);
        webInteractions.centerElement(element, elementName, pageName);
    }

    /**
     * Clicks towards the specified element on the specified page.
     *
     * @param elementName The name or identifier of the element to click towards.
     * @param pageName    The name or identifier of the page where the element is located.
     */
    @Given("^Click towards the (\\w+) on the (\\w+)$")
    public void clickTowardsElement(String elementName, String pageName) {
        WebElement element = objectRepository.acquireElementFromPage(elementName, pageName);
        getInteractions(element).clickTowards(element, elementName, pageName);
    }

    /**
     * Clicks on the specified element if it is present on the page.
     *
     * @param elementName the name of the element to click on
     * @param pageName    the name of the page containing the element
     * @throws WebDriverException if the element cannot be found or clicked
     */
    @Given("^If present, click the (\\w+) on the (\\w+)$")
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
        WebElement element = getInteractions(elements.get(0)).scrollInList(elementName, elements);
        getInteractions(element).clickElement(element, elementName, pageName);
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
    public void clickListedButtonByAttribute(String attributeValue, String attributeName, String listName, String pageName) {WebElement element = objectRepository.acquireListedElementByAttribute(attributeName, attributeValue, listName, pageName);
        getInteractions(element).clickElement(element, attributeName + " attribute named element", pageName);
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
     * @param pageName  the name of the page containing the element
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
        getInteractions(element).verifyContainsText(element,elementName,pageName,expectedText);
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
     * Verify that a listed element from a specific page has a specific value for its attribute.
     *
     * @param elementName    the name of the element to be verified
     * @param listName       the name of the list containing the element
     * @param pageName       the name of the page containing the list
     * @param attributeValue the expected value of the attribute
     * @param attributeName  the name of the attribute to be verified
     */
    @Given("^Verify that element ([A-Za-z0-9 ]+) from (\\w+) list on the (\\w+) has (.+?(?:\\s+.+?)*) value for its (\\w+) attribute$")
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
     * @param listName     The name or identifier of the list containing the elements to verify.
     * @param pageName     The name or identifier of the page where the list is located.
     * @param expectedText The expected text that each listed element should contain.
     */
    @Given("Perform text verification for listed elements of {} list on the {} contains {}")
    public void textVerificationForListedElement(
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
    @Given("Assert that value of {} is contains {}")
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
     * Executes a specified mobile command
     *
     * @param command search, go, done, next, previous, send, return, clear
     */
    @Given("Execute mobile editor command: {}")
    public void executeMobileEditCommand(String command) {
        PickleibAppiumDriver.get().executeScript("mobile:performEditorAction", ImmutableMap.of("action", command));
    }
    /**
     * Executes a specified mobile command
     *
     * @param command search, go, done, next, previous, send, return, clear
     */
    @Given("Execute {} mobile command with {} parameter for {} from {}")
    public void executeGenericMobileCommand(String command, String parameter, String elementName, String pageName) {
        Map<String, String> params = new HashMap<>();
        platformInteractions.scrollUntilFound(elementName);
        WebElement element = platformInteractions.getElementByText(elementName, pageName);
        params.put(parameter, element.getAttribute("resourceId"));
        PickleibAppiumDriver.get().executeScript("mobile: " + command, params);
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
     * Fills the input field from a given page with a specified file.
     *
     * @param inputName     the name of the input field to be filled.
     * @param pageName      the name of the page containing the element.
     * @param path          the file path of the file to be uploaded.
     */
    @Given("Upload file on input {} on the {} with file: {}")
    public void uploadFile(String inputName, String pageName, String path) {
        WebElement inputElement = objectRepository.acquireElementFromPage(inputName, pageName);
        webInteractions.fillInputElement(inputElement, path, false, false);
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
     * Save context value to another context key to save it before it will be overwritten.
     */
    @Given("Save context value from {} context key to {}")
    public void saveContextValueFromOneContextKeyToAnother(String contextKey, String newContextKey) {
        Object value = ContextStore.get(contextKey);
        ContextStore.put(newContextKey, value);
        log.info("Value of " + markup(BLUE, contextKey) + markup(GREEN, " was saved to ") + markup(BLUE, newContextKey));
    }

    /**
     * Executes a JavaScript script on a web element identified by its text content.
     * <p>
     * This method locates a web element containing the specified text and executes the provided
     * JavaScript script on it using the configured web interactions utility.
     * </p>
     *
     * @param script       The JavaScript code to execute on the target element.
     * @param elementText  The text content used to identify the target web element.
     */
    @Given("Execute script {string} on element with text {string}")
    public void executeScript(String script, String elementText) {
        WebElement element = webInteractions.getElementContainingText(elementText);
        webInteractions.executeScript(script, element);
    }

}