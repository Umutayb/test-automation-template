package steps;

import io.cucumber.java.*;
import io.cucumber.java.en.*;
import models.common.ObjectRepository;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Printer;
import utils.ScreenCaptureUtility;
import utils.WebUtilities;
import utils.driver.Driver;

import static utils.WebUtilities.Color.*;

public class CommonSteps extends WebUtilities {

    public Scenario scenario;

    Driver browser = new Driver();
    Printer log = new Printer(CommonSteps.class);
    ScreenCaptureUtility capture = new ScreenCaptureUtility();

    @Before
    public void before(Scenario scenario){
        log.new Info("Running: " + highlighted(PURPLE, scenario.getName()));
        this.scenario = scenario;
        browser.initialize(
                properties.getProperty("website-username"),
                properties.getProperty("website-password")
        );
    }

    @After
    public void kill(Scenario scenario){
        if (scenario.isFailed())
            capture.captureScreen(
                    scenario.getName().replaceAll(" ","")+"@"+scenario.getLine(), driver
            );
        else log.new Success(scenario.getName() + ": PASS!");
        browser.terminate();
    }

    @Given("Navigate to url: {}")
    public void getUrl(String url) {navigate(url);}

    @Given("Navigate to the test page")
    public void getTestUrl() {navigate(properties.getProperty("test-url"));}

    @Given("Navigate to the acceptance page")
    public void getAccUrl() {navigate(properties.getProperty("acc-url"));}

    @Given("Set window width & height as {} & {}")
    public void setFrameSize(Integer width, Integer height) {setWindowSize(width,height);}

    @Given("Refresh the page")
    public void refresh() {
        refreshThePage();
    }

    @Given("Navigate browser {}")
    public void browserNavigate(String direction) {navigateBrowser(Navigation.valueOf(direction.toUpperCase()));}

    @Given("Click button with {} text")
    public void clickWithText(String text) {clickButtonWithText(text, true);}

    @Given("Wait {} seconds")
    public void wait(Integer duration) {
        waitFor(duration);
    }

    @Given("Take a screenshot")
    public void takeAScreenshot() {capture.captureScreen(scenario.getName().replaceAll(" ",""), driver);}

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

    @Given("Fill the {} on the {} with text: {}")
    public void fill(String inputName, String pageName, String input){
        log.new Info("Filling " +
                highlighted(BLUE, inputName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName) +
                highlighted(GRAY, " with the text: ") +
                highlighted(BLUE, input)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        clearFillInput(getElementFromPage(inputName, pageName, new ObjectRepository()), input, false, true);
    }

    @Given("Fill component element {} of {} component on the {} with text: {}")
    public void fill(String inputName, String componentName, String pageName, String input){
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

    @Given("Verify the text of {} on the {} to be: {}")
    public void verifyText(String elementName, String pageName, String expectedText){
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
        Assert.assertTrue(elementIs(element, ElementState.DISPLAYED, System.currentTimeMillis()));
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
        WebElement element = getElementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        Assert.assertTrue(elementIs(element, ElementState.DISPLAYED, System.currentTimeMillis()));
        log.new Success("Presence of the element " + elementName + " was verified!");
    }

    @Given("Verify that element {} on the {} is in {} state")
    public void verifyEnabled(String elementName, String pageName, String expectedState){
        log.new Info("Verifying " +
                highlighted(BLUE, expectedState) +
                highlighted(GRAY," state of ") +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        WebElement element = getElementFromPage(elementName, pageName, new ObjectRepository());
        Assert.assertTrue(elementIs(element, ElementState.valueOf(expectedState), System.currentTimeMillis()));
        log.new Success("The element " + elementName + " was verified to be enabled!");
    }

    @Given("Verify that component element {} of {} on the {} is in {} state")
    public void verifyEnabled(String elementName, String componentName, String pageName, String expectedState){
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
        Assert.assertTrue(elementIs(element, ElementState.valueOf(expectedState), System.currentTimeMillis()));
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
        WebElement element = getElementFromPage(elementName,pageName, new ObjectRepository());
        wait.until(ExpectedConditions.invisibilityOf(element));
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
        wait.until(ExpectedConditions.invisibilityOf(element));
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
        wait.until(ExpectedConditions.visibilityOf(element));
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
        wait.until(ExpectedConditions.visibilityOf(element));
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
        wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue));
    }

    @Given("Wait until component element {} of {} on the {} has {} value for its {} attribute")
    public void waitUntilElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        pageName = strUtils.firstLetterDeCapped(pageName);
        componentName = strUtils.firstLetterDeCapped(componentName);
        WebElement element = getElementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue));
    }

    @Given("Verify that element {} on the {} has {} value for its {} attribute")
    public void verifyElementContainsAttribute(
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
        Assert.assertTrue(
                "The " + attributeName + " attribute of element " + elementName + " could not be verified." +
                        "\nExpected value: " + attributeValue + "\nActual value: " + element.getAttribute(attributeName),
                wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue))
        );
    }

    @Given("Verify that component element {} of {} on the {} has {} value for its {} attribute")
    public void verifyElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        log.new Info("Waiting for the absence of " +
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
}