package steps;

import common.EmailInbox;
import context.ContextStore;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import common.ObjectRepository;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.*;
import utils.driver.Driver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static utils.WebUtilities.Color.*;

public class CommonSteps extends WebUtilities {

    public Scenario scenario;

    Driver browser = new Driver();
    EmailInbox emailInbox = new EmailInbox();
    ScreenCaptureUtility capture = new ScreenCaptureUtility();

    @Before
    public void before(Scenario scenario){
        log.new Info("Running: " + highlighted(PURPLE, scenario.getName()));
        this.scenario = scenario;
        browser.initialize();
        ObjectRepository.environment = null;
    }

    @After
    public void kill(Scenario scenario){
        if (scenario.isFailed()) {
            capture.captureScreen(
                    scenario.getName().replaceAll(" ", "") + "@" + scenario.getLine(), driver
            );
        }
        else log.new Success(scenario.getName() + ": PASS!");
        browser.terminate();
        if (scenario.isFailed()) throw new RuntimeException(scenario.getName() + ": FAILED!");
    }

    @Given("Navigate to url: {}")
    public void getUrl(String url) {
        url = contextCheck(url);
        navigate(url);}

    @Given("Go to the {} page")
    public void toPage(String page){
        String url = driver.getCurrentUrl();
        String pageUrl = url + page;
        navigate(pageUrl);
    }

    @Given("Get email at {}")
    public void getHTML(String url) {
        log.new Info("Navigating to the email @" + url);
        driver.get(contextCheck(url));
    }

    @Given("Navigate to the test page")
    public void getTestUrl() {
        navigate(properties.getProperty("test-url"));
        ObjectRepository.environment = ObjectRepository.Environment.TEST;
    }

    @Given("Navigate to the test page with credentials")
    public void getTestUrlWithCredentials() {
        String username = properties.getProperty("website-username");
        String password = properties.getProperty("website-password");
        // appending username, password with URL
        String url;
        if (ObjectRepository.environment == null) // We check if the env is null so that we do not set credentials twice
            url = "https://" + username + ":" + password + "@" + properties.getProperty("test-url");
        else url = "https://" + properties.getProperty("test-url");
        navigate(url);
        ObjectRepository.environment = ObjectRepository.Environment.TEST;
    }

    @Given("Navigate to the acceptance page")
    public void getAccUrl() {
        String username = properties.getProperty("website-username");
        String password = properties.getProperty("website-password");
        // appending username, password with URL
        String url;
        if (ObjectRepository.environment == null) // We check if the env is null so that we do not set credentials twice
            url = "https://" + username + ":" + password + "@" + properties.getProperty("acc-url");
        else url = "https://" + properties.getProperty("acc-url");
        navigate(url);
        ObjectRepository.environment = ObjectRepository.Environment.ACCEPTANCE;
    }

    @Given("Set window width & height as {} & {}")
    public void setFrameSize(Integer width, Integer height) {setWindowSize(width,height);}

    @Given("Refresh the page")
    public void refresh() {refreshThePage();}

    @Given("Delete cookies")
    public void deleteCookies() {driver.manage().deleteAllCookies();}

    @Given("^Navigate browser (BACKWARDS|FORWARDS)$")
    public void browserNavigate(Navigation direction) {navigateBrowser(direction);}

    @Given("Click button with {} text")
    public void clickWithText(String text) {clickButtonWithText(text, true);}

    @Given("Click button with {} css locator")
    public void clickWithLocator(String text) {
        WebElement element = driver.findElement(By.cssSelector(text));
        clickElement(element, true);
    }

    @Given("Wait {} seconds")
    public void wait(Integer duration) {
        waitFor(duration);
    }

    @Given("^Scroll (UP|DOWN)$")
    public void scrollTo(Direction direction){scroll(direction);}

    @Given("Take a screenshot")
    public void takeAScreenshot() {capture.captureScreen(scenario.getName().replaceAll(" ","_"), driver);}

    @Given("Click the {} on the {}")
    public void click(String buttonName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        clickElement(getElementFromPage(buttonName, pageName, new ObjectRepository()), true);
    }

    @Given("Acquire the attribute {} of {} on the {}")
    public void getAttributeValue(String attributeName, String elementName, String pageName){
        log.new Info("Acquiring " +
                highlighted(BLUE,attributeName) +
                highlighted(GRAY," attribute of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName, pageName, new ObjectRepository());
        String attribute = element.getAttribute(attributeName);
        log.new Info("Attribute -> " + highlighted(BLUE, attributeName) + highlighted(GRAY," : ") + attribute);
        ContextStore.put(elementName + "-" + attributeName,attribute);
    }

    @Given("Center the {} on the {}")
    public void center(String elementName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        centerElement(getElementFromPage(elementName, pageName, new ObjectRepository()));
    }

    @Given("Click towards the {} on the {}")
    public void clickTowards(String buttonName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        clickAtAnOffset(getElementFromPage(buttonName, pageName, new ObjectRepository()), 0, 0, false);
    }

    //TODO: Step to scroll element into view

    @Given("Click component element {} of {} component on the {}")
    public void click(String buttonName, String componentName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        clickElement(getElementFromComponent(buttonName, componentName, pageName, new ObjectRepository()), true);
    }

    @Given("Center component element {} of {} component on the {}")
    public void center(String elementName, String componentName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        centerElement(getElementFromComponent(elementName, componentName, pageName, new ObjectRepository()));
    }

    @Given("Click towards component element {} of {} component on the {}")
    public void clickTowards(String buttonName, String componentName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        clickAtAnOffset(getElementFromComponent(buttonName, componentName, pageName, new ObjectRepository()), 0, 0, false);
    }

    @Given("If present, click the {} on the {}")
    public void clickIfPresent(String buttonName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, ", if present...")
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        try {
            WebElement element = getElementFromPage(buttonName, pageName, new ObjectRepository());
            if (elementIs(element, ElementState.DISPLAYED)) clickElement(element, true);
        }
        catch (WebDriverException ignored){log.new Warning("The " + buttonName + " was not present");}
    }

    @Given("If present, click component element {} of {} component on the {}")
    public void clickIfPresent(String buttonName, String componentName, String pageName){
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, ", if present...")
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        try {
            WebElement element = getElementFromComponent(buttonName, componentName, pageName, new ObjectRepository());
            if (elementIs(element, ElementState.DISPLAYED)) clickElement(element, true);
        }
        catch (WebDriverException ignored){log.new Warning("The " + buttonName + " was not present");}
    }

    @Given("Click listed element {} from {} list on the {}")
    public void clickListedButton(String buttonName, String listName, String pageName){
        List<WebElement> elements = getElementsFromPage(
                listName,
                strUtils.firstLetterDeCapped(pageName),
                new ObjectRepository()
        );
        WebElement element = acquireNamedElementAmongst(elements, buttonName);
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        clickElement(element, true);
    }

    @Given("Click listed component element {} of {} from {} list on the {}")
    public void clickListedButton(String buttonName, String componentName, String listName, String pageName){
        List<WebElement> elements = getElementsFromComponent(
                listName,
                strUtils.firstLetterDeCapped(componentName),
                strUtils.firstLetterDeCapped(pageName),
                new ObjectRepository()
        );
        WebElement element = acquireNamedElementAmongst(elements, buttonName);
        log.new Info("Clicking " +
                highlighted(BLUE, buttonName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        clickElement(element, true);
    }

    @Given("Click listed attribute element that has {} value for its {} attribute from {} list on the {}")
    public void clickListedButtonByAttribute(String attributeValue, String attributeName, String listName, String pageName) {
        List<WebElement> elements = getElementsFromPage(
                listName,
                strUtils.firstLetterDeCapped(pageName),
                new ObjectRepository()
        );
        WebElement element = acquireElementUsingAttributeAmongst(elements, attributeName, attributeValue);
        log.new Info("Clicking " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " on the ") +
                highlighted(BLUE, pageName)
        );
        clickElement(element, true);
    }

    @Given("Click listed attribute element of {} component that has {} value for its {} attribute from {} list on the {}")
    public void clickListedButtonByAttribute(String componentName, String attributeValue, String attributeName, String listName, String pageName) {
        List<WebElement> elements = getElementsFromComponent(
                listName,
                strUtils.firstLetterDeCapped(componentName),
                strUtils.firstLetterDeCapped(pageName),
                new ObjectRepository()
        );
        WebElement element = acquireElementUsingAttributeAmongst(elements, attributeName, attributeValue);
        log.new Info("Clicking " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " on the ") +
                highlighted(BLUE, pageName)
        );
        clickElement(element, true);
    }

    @Given("Fill listed input {} from {} list on the {} with text: {}")
    public void fillListedInput(String inputName, String listName, String pageName, String input){
        input = contextCheck(input);
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, input)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        List<WebElement> elements = getElementsFromPage(listName, pageName, new ObjectRepository());
        WebElement element = acquireNamedElementAmongst(elements, inputName);
        clearFillInput(element, input, false, true);
    }

    @Given("Fill the input {} on the {} with text: {}")
    public void fill(String inputName, String pageName, String input){
        input = contextCheck(input);
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, input)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        clearFillInput(
                getElementFromPage(inputName, pageName, new ObjectRepository()), //Element
                input,
                false,
                true
        );
    }

    @Given("Fill component input {} of {} component on the {} with text: {}")
    public void fill(String inputName, String componentName, String pageName, String input){
        input = contextCheck(input);
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, input)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        clearFillInput(
                getElementFromComponent(inputName, componentName, pageName, new ObjectRepository()), //Element
                input,
                false,
                true
        );
    }

    @Given("Upload file on the input {} on the {} with file: {}")
    public void fillInputWithFile(String inputName, String pageName, String input){
        input = contextCheck(input);
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, input)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        clearFillInput(
                getElementFromPage(inputName, pageName, new ObjectRepository()), //Element
                input,
                false,
                false
        );
    }

    @Given("Upload file on component input {} of {} component on the {} with file: {}")
    public void fillInputWithFile(String inputName, String componentName, String pageName, String input){
        input = contextCheck(input);
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, input)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        clearFillInput(
                getElementFromComponent(inputName, componentName, pageName, new ObjectRepository()), //Element
                input,
                false,
                false
        );
    }

    @Given("Fill component form input on the {}")
    public void fillForm(String pageName, DataTable table){
        List<Map<String, String>> signForms = table.asMaps();
        String inputName;
        String input;
        for (Map<String, String> form : signForms) {
            inputName = form.get("Input Element");
            input = contextCheck(form.get("Input"));
            log.new Info("Filling " +
                    highlighted(BLUE, inputName) +
                    highlighted(GRAY," on the ") +
                    highlighted(BLUE, pageName) +
                    highlighted(GRAY, " with the text: ") +
                    highlighted(BLUE, input)
            );
            pageName = strUtils.firstLetterDeCapped(pageName);
            clearFillInput(getElementFromPage(inputName, pageName, new ObjectRepository()), //Element
                    input,
                    false,
                    true
            );
        }
    }

    @Given("Fill component form input of {} component on the {}")
    public void fillForm(String componentName, String pageName, DataTable table){
        List<Map<String, String>> forms = table.asMaps();
        String inputName;
        String input;
        for (Map<String, String> form : forms) {
            inputName = form.get("Input Element");
            input = contextCheck(form.get("Input"));
            log.new Info("Filling " +
                    highlighted(BLUE, inputName) +
                    highlighted(GRAY," on the ") +
                    highlighted(BLUE, pageName) +
                    highlighted(GRAY, " with the text: ") +
                    highlighted(BLUE, input)
            );
            pageName = strUtils.firstLetterDeCapped(pageName);
            componentName = strUtils.firstLetterDeCapped(componentName);
            clearFillInput(
                    getElementFromComponent(inputName, componentName, pageName, new ObjectRepository()), //Input element
                    input,
                    false,
                    true
            );
        }
    }

    @Given("Fill iFrame element {} of {} on the {} with text: {}")
    public void fillIframeInput(String inputName,String iframeName,String pageName, String inputText){
        inputText = contextCheck(inputText);
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," i-frame element input on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, inputText)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement iframe = getElementFromPage(iframeName, pageName, new ObjectRepository());
        driver.switchTo().frame(waitUntilElementIs(iframe, ElementState.DISPLAYED, false));
        WebElement element = getElementFromPage(inputName, pageName, new ObjectRepository());
        clearFillInput(element, inputText,true,true);
        driver.switchTo().parentFrame();
    }

    @Given("Click i-frame element {} in {} on the {}")
    public void clickIframeElement(String elementName,String iframeName,String pageName ){
        log.new Info("Clicking the i-frame element " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement iframe = getElementFromPage(iframeName, pageName, new ObjectRepository());
        driver.switchTo().frame(iframe);
        WebElement element = getElementFromPage(elementName, pageName, new ObjectRepository());
        click(element);
    }

    @Given("Fill iframe component form input of {} component on the {}")
    public void fillFormIframe(String iframeName, String componentName, String pageName, DataTable table){
        List<Map<String, String>> forms = table.asMaps();
        String inputName;
        String input;
        for (Map<String, String> form : forms) {
            inputName = form.get("Input Element");
            input = contextCheck(form.get("Input"));
            log.new Info("Filling " +
                    highlighted(BLUE, inputName) +
                    highlighted(GRAY," on the ") +
                    highlighted(BLUE, pageName) +
                    highlighted(GRAY, " with the text: ") +
                    highlighted(BLUE, input)
            );
            pageName = strUtils.firstLetterDeCapped(pageName);
            componentName = strUtils.firstLetterDeCapped(componentName);
            WebElement element = getElementFromPage(iframeName, pageName, new ObjectRepository());
            driver.switchTo().frame(element);

            clearFillInput(
                    getElementFromComponent(inputName, componentName, pageName, new ObjectRepository()), //Input element
                    input,
                    false,
                    true
            );
        }
    }

    @Given("Fill listed component input {} of {} component on the {} with text: {}")
    public void fillListedInput(String inputName, String listName, String componentName, String pageName, String input){
        input = contextCheck(input);
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, input)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        List<WebElement> elements = getElementsFromComponent(listName, componentName, pageName, new ObjectRepository());
        WebElement element = acquireNamedElementAmongst(elements, inputName);
        clearFillInput(element, input, false, true);
    }

    @Given("Verify the text of {} on the {} to be: {}")
    public void verifyText(String elementName, String pageName, String expectedText){
        expectedText = contextCheck(expectedText);
        log.new Info("Performing text verification for " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        Assert.assertEquals(expectedText, getElementFromPage(elementName, pageName, new ObjectRepository()).getText());
        log.new Success("Text of the element " + elementName + " was verified!");
    }

    @Given("Verify text of the component element {} of {} on the {} to be: {}")
    public void verifyText(String elementName, String componentName, String pageName, String expectedText){
        expectedText = contextCheck(expectedText);
        log.new Info("Performing text verification for " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        Assert.assertEquals(
                expectedText,
                getElementFromComponent(elementName, componentName, pageName, new ObjectRepository()).getText()
        );
        log.new Success("Text of the element " + elementName + " was verified!");
    }

    @Given("Verify presence of element {} on the {}")
    public void verifyPresence(String elementName, String pageName){
        log.new Info("Verifying presence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName, pageName, new ObjectRepository());
        waitUntilElementIs(element, ElementState.DISPLAYED, true);
        log.new Success("Presence of the element " + elementName + " was verified!");
    }

    @Given("Verify presence of the component element {} of {} on the {}")
    public void verifyPresence(String elementName, String componentName, String pageName){
        log.new Info("Verifying presence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        WebElement element = getElementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        waitUntilElementIs(element, ElementState.DISPLAYED, true);
        log.new Success("Presence of the element " + elementName + " was verified!");
    }

    @Given("Verify that element {} on the {} is in {} state")
    public void verifyState(String elementName, String pageName, String expectedState){
        log.new Info("Verifying " +
                highlighted(BLUE, expectedState) +
                highlighted(GRAY," state of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName, pageName, new ObjectRepository());
        waitUntilElementIs(element, ElementState.valueOf(expectedState), true);
        log.new Success("The element '" + elementName + "' was verified to be enabled!");
    }

    @Given("Verify that component element {} of {} on the {} is in {} state")
    public void verifyState(String elementName, String componentName, String pageName, String expectedState){
        log.new Info("Verifying " +
                highlighted(BLUE, expectedState) +
                highlighted(GRAY," state of ")+
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        expectedState = expectedState.toUpperCase();
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        WebElement element = getElementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        waitUntilElementIs(element, ElementState.valueOf(expectedState), true);
        log.new Success("The element " + elementName + " was verified to be enabled!");
    }

    @Given("Wait for absence of element {} on the {}")
    public void waitUntilAbsence(String elementName, String pageName){
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName, pageName, new ObjectRepository());
        elementIs(element, ElementState.ABSENT);
    }

    @Given("Wait for absence of component element {} of {} on the {}")
    public void waitUntilAbsence(String elementName, String componentName, String pageName){
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        WebElement element = getElementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        elementIs(element, ElementState.ABSENT);
    }

    @Given("Wait for element {} on the {} to be visible")
    public void waitUntilVisible(String elementName, String pageName){
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName,pageName, new ObjectRepository());
        elementIs(element, ElementState.DISPLAYED);
    }

    @Given("Wait for component element {} of {} on the {} to be visible")
    public void waitUntilVisible(String elementName, String componentName, String pageName){
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        WebElement element = getElementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        elementIs(element, ElementState.DISPLAYED);
    }

    @Given("Wait until element {} on the {} has {} value for its {} attribute")
    public void waitUntilElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName,pageName, new ObjectRepository());
        try {wait.until((ExpectedConditions.attributeContains(element, attributeName, attributeValue)));}
        catch (WebDriverException ignored) {}
    }

    @Given("Wait until component element {} of {} on the {} has {} value for its {} attribute")
    public void waitUntilElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Waiting for the presence of " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " attribute of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        WebElement element = getElementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        try {wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue));}
        catch (WebDriverException ignored) {}
    }

    @Given("Verify that element {} on the {} has {} value for its {} attribute")
    public void verifyElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Verifying " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " attribute of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName,pageName, new ObjectRepository());
        Assert.assertTrue(
                "The " + attributeName + " attribute of element " + elementName + " could not be verified." +
                        "\nExpected value: " + attributeValue + "\nActual value: " + element.getAttribute(attributeName),
                wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue))
        );
    }

    @Given("Verify {} css attribute of element {} on the {} is {} ")
    public void verifyElementColor(
            String attributeName,
            String elementName,
            String pageName,
            String attributeValue) {
        log.new Info("Verifying " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " attribute of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName,pageName, new ObjectRepository());
        Assert.assertEquals(
                "The " + attributeName + " attribute of element " + elementName + " could not be verified." +
                        "\nExpected value: " + attributeValue + "\nActual value: " + element.getCssValue(attributeName),
                attributeValue
        );
    }

    @Given("Verify that component element {} of {} on the {} has {} value for its {} attribute")
    public void verifyElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Verifying " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " attribute of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        WebElement element = getElementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        Assert.assertTrue(
                "The " + attributeName + " attribute of element " + elementName + " could not be verified." +
                        "\nExpected value: " + attributeValue + "\nActual value: " + element.getAttribute(attributeName),
                wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue))
        );
    }

    @Given("Verify that element {} from {} list on the {} has {} value for its {} attribute")
    public void verifyListedElementContainsAttribute(
            String elementName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Verifying " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " attribute of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        List<WebElement> elements = getElementsFromPage(listName, pageName, new ObjectRepository());
        WebElement element = acquireNamedElementAmongst(elements, elementName);
        Assert.assertTrue(
                "The " + attributeName + " attribute of element " + elementName + " could not be verified." +
                        "\nExpected value: " + attributeValue + "\nActual value: " + element.getAttribute(attributeName),
                wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue))
        );
    }

    @Given("Verify text of listed element {} from the {} on the {} contains: {}")
    public void verifyListedElementContainsText(
            String elementName,
            String listName,
            String pageName,
            String expectedText) {
        expectedText = contextCheck(expectedText);
        log.new Info("Verifying text of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        List<WebElement> elements = getElementsFromPage(listName, pageName, new ObjectRepository());
        WebElement element = acquireNamedElementAmongst(elements, elementName);
        Assert.assertTrue(
                "The " + elementName + " does not contain text '" + expectedText + "' ",
                element.getText().contains(expectedText)
        );
    }

    @Given("Verify text of listed component element {} from the {} of {} on the {} is equal to {}")
    public void verifyListedComponentElementContainsText(
            String elementName,
            String componentName,
            String listName,
            String pageName,
            String text) {
        log.new Info("Verifying text of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        List<WebElement> elements = getElementsFromComponent(listName, componentName, pageName, new ObjectRepository());
        WebElement element = acquireNamedElementAmongst(elements, elementName);
        Assert.assertTrue(
                "The " + elementName + " does not contain text '" + text + "' ",
                element.getText().contains(text)
        );
    }

    @Given("Verify that component element {} of {} from {} list on the {} has {} value for its {} attribute")
    public void verifyListedElementContainsAttribute(
            String elementName,
            String componentName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Verifying " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " attribute of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        List<WebElement> elements = getElementsFromComponent(listName, componentName, pageName, new ObjectRepository());
        WebElement element = acquireNamedElementAmongst(elements, elementName);
        Assert.assertTrue(
                "The " + attributeName + " attribute of element " + elementName + " could not be verified." +
                        "\nExpected value: " + attributeValue + "\nActual value: " + element.getAttribute(attributeName),
                wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue))
        );
    }

    @Given("Acquire & save email with {} -> {}")
    public void acquireEmail(EmailUtilities.Inbox.EmailField filterType, String filterKey) {
        log.new Info("Acquiring & saving email(s) by " +
                highlighted(BLUE, filterType.name()) +
                highlighted(GRAY, " -> ") +
                highlighted(BLUE, filterKey)
        );
        emailInbox.getEmail(filterType, filterKey, false, true);

        File dir = new File("inbox");
        String absolutePath = null;
        for (File email : Objects.requireNonNull(dir.listFiles()))
            try {
                boolean nullCheck = Files.probeContentType(email.toPath())!=null;
                if (nullCheck && Files.probeContentType(email.toPath()).equals("text/html")) {
                    absolutePath = "file://" + email.getAbsolutePath().replaceAll("#","%23");
                    break;
                }
            }
            catch (IOException e) {throw new RuntimeException(e);}

        ContextStore.put("emailPath", absolutePath);
    }

    @Given("Verify the page is redirecting to the register page {}")
    public void verifyUrl(String url) {
        url = contextCheck(url);
        log.new Info("The url contains " + url);
        log.new Info("The current url contains " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains(url));
    }


    @Given("Click the specific text {} button")
    public void clickButtonWithText(String buttonText, Boolean scroll) {
        this.clickElement(this.getElementByText(buttonText), scroll);
    }
}