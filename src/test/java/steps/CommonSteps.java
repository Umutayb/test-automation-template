package steps;

import bookstore.BookStoreAuthorisation;
import bookstore.models.CredentialModel;
import bookstore.models.TokenResponseModel;
import bookstore.models.UserResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.webdriverextensions.WebComponent;
import common.EmailInbox;
import common.LogUtility;
import context.ContextStore;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import common.ObjectRepository;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.html5.RemoteWebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pickleib.driver.Driver;
import pickleib.driver.DriverFactory;
import pickleib.element.ElementAcquisition;
import pickleib.element.ElementInteractions;
import pickleib.utilities.ScreenCaptureUtility;
import pickleib.utilities.WebUtilities;
import records.Bundle;
import utils.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static utils.StringUtilities.Color.*;


public class CommonSteps extends WebUtilities {

    public Scenario scenario;
    public boolean authenticate;
    public boolean initialiseBrowser;
    LogUtility logUtil = new LogUtility();
    EmailInbox emailInbox = new EmailInbox();
    ObjectMapper objectMapper = new ObjectMapper();
    ScreenCaptureUtility capture = new ScreenCaptureUtility();
    ElementInteractions interact = new ElementInteractions();
    ElementAcquisition.PageObjectModel acquire = new ElementAcquisition.PageObjectModel();

    public CommonSteps(){
        PropertyUtility.loadProperties("src/test/resources/test.properties");
        logUtil.setLogLevel(logUtil.getLogLevel(properties.getProperty("system-log-level", "error")));
    }

    @SuppressWarnings("unused")
    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object transformer(Object fromValue, Type toValueType) {
        return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
    }

    @Before
    public void before(Scenario scenario){
        log.new Info("Running: " + highlighted(PURPLE, scenario.getName()));
        processScenarioTags(scenario);
        if (initialiseBrowser) {
            DriverFactory.DriverType driverType = getDriverType(scenario);
            if (driverType!=null) Driver.initialize(driverType);
            else Driver.initialize();
        }
        if (authenticate) {
            CredentialModel user = new CredentialModel("Booker");
            user.setPassword("Bookersbooks1*");

            UserResponseModel userResponseModel = BookStoreAuthorisation.createUser(user);
            ContextStore.put("contextUser", user);

            ContextStore.put("userId", userResponseModel.getUserID());
            ContextStore.put("userName", userResponseModel.getUsername());
            ContextStore.put("password", user.getPassword());

            TokenResponseModel tokenResponse = BookStoreAuthorisation.generateToken(user);
            ContextStore.put("token", tokenResponse.getToken());
        }
        ObjectRepository.environment = null;
    }

    @After
    public void kill(Scenario scenario) {
        if (initialiseBrowser) {
            if (scenario.isFailed()) {
                capture.captureScreen(
                        scenario.getSourceTagNames()
                                .stream()
                                .filter(tag -> tag.contains("SCN-"))
                                .collect(Collectors.joining())
                                .replaceAll("SCN-", ""),
                        driver
                );
            }
            org.openqa.selenium.remote.SessionId sessionId = driver.getSessionId();
            Driver.terminate();
        }
        if (scenario.isFailed()) throw new RuntimeException(scenario.getName() + ": FAILED!");
        else log.new Success(scenario.getName() + ": PASS!");
    }

    public void processScenarioTags(Scenario scenario){
        log.new Important(scenario.getSourceTagNames().toString());
        this.scenario = scenario;
        authenticate = scenario.getSourceTagNames().contains("@Authenticate");
        initialiseBrowser = scenario.getSourceTagNames().contains("@Web-UI");
    }

    public DriverFactory.DriverType getDriverType(Scenario scenario) {
        for (DriverFactory.DriverType driverType: DriverFactory.DriverType.values()) {
            if (scenario.getSourceTagNames().stream().anyMatch(tag -> tag.replaceAll("@", "").equalsIgnoreCase(driverType.name())))
                return driverType;
        }
        return null;
    }

    @Given("Navigate to url: {}")
    public void getUrl(String url) {
        url = strUtils.contextCheck(url);
        driver.get(url);
    }

    @Given("Go to the {} page")
    public void toPage(String page){
        String url = driver.getCurrentUrl();
        String pageUrl = url + page;
        navigate(pageUrl);
    }

    @Given("Switch to the next tab")
    public void switchTab() {
        String parentHandle = switchWindowByHandle(null);
        ContextStore.put("parentHandle", parentHandle);
    }

    @Given("Switch back to the parent tab")
    public void switchToParentTab() {
        switchWindowByHandle(ContextStore.get("parentHandle").toString());
    }

    @Given("Switch to the tab with handle: {}")
    public void switchTab(String handle) {
        handle = strUtils.contextCheck(handle);
        String parentHandle = switchWindowByHandle(handle);
        ContextStore.put("parentHandle", parentHandle);
    }

    @Given("Switch to the tab number {}")
    public void switchTab(Integer handle) {
        String parentHandle = switchWindowByIndex(handle);
        ContextStore.put("parentHandle", parentHandle);
    }

    @Given("Get email at {}")
    public void getHTML(String url) {
        url = strUtils.contextCheck(url);
        log.new Info("Navigating to the email @" + url);
        driver.get(url);
    }

    @Given("^Navigate to the (acceptance|test|dev) page$")
    public void getURL(ObjectRepository.Environment environment) {
        String username = properties.getProperty("website-username");
        String password = properties.getProperty("website-password");
        String protocol = properties.getProperty("protocol", "HTTPS").toLowerCase();
        String baseUrl = properties.getProperty(environment.getUrlKey());
        String url = protocol + "://" + baseUrl;
        // appending username, password with URL
        // We check if the env is null so that we do not set credentials twice
        if (ObjectRepository.environment == null && username != null && password != null) url = protocol + "://" + username + ":" + password + "@" + baseUrl;
        log.new Info("Navigating to " + highlighted(BLUE, url));
        driver.get(url);
        ObjectRepository.environment = environment;
    }

    @Given("Set window width & height as {} & {}")
    public void setFrameSize(Integer width, Integer height) {setWindowSize(width,height);}

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

    @Given("Add the following cookies:")
    public void addCookies(DataTable cookieTable){
        Map<String, String> cookies = cookieTable.asMap();
        for (String cookieName: cookies.keySet()) {
            Cookie cookie = new Cookie(cookieName, strUtils.contextCheck(cookies.get(cookieName)));
            driver.manage().addCookie(cookie);
        }
    }

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
        interact.clickButtonByText(text, true);
    }

    @Given("Wait {} seconds")
    public void wait(Integer duration) {
        interact.wait(duration);
    }

    @Given("^Scroll (UP|DOWN)$")
    public void scrollTo(Direction direction){interact.scrollInDirection(direction);}

    @Given("Take a screenshot")
    public void takeAScreenshot() {capture.captureScreen(scenario.getName().replaceAll(" ","_"), driver);}

    @Given("Click the {} on the {}")
    public void click(String buttonName, String pageName){
        WebElement element = acquire.elementFromPage(buttonName, pageName, new ObjectRepository());
        interact.clickStep(element, buttonName, pageName);
    }

    @Given("Acquire the {} attribute of {} on the {}")
    public void getAttributeValue(String attributeName, String elementName, String pageName){
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.saveAttributeValue(element,attributeName,elementName,pageName);
    }

    @Given("Acquire attribute {} from component element {} of {} component on the {}") // Use 'innerHTML' attributeName to acquire text on an element
    public void getAttributeValue(String attributeName, String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.saveAttributeValue(element,attributeName,elementName,pageName);
    }

    @Given("Center the {} on the {}")
    public void center(String elementName, String pageName){
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.center(element, elementName, pageName);
    }

    @Given("Click towards the {} on the {}")
    public void clickTowards(String elementName, String pageName) {
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.clickTowards(element, elementName, pageName);
    }

    @Given("Click component element {} of {} component on the {}")
    public void click(String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.clickStep(element, elementName, pageName);
    }

    @Given("Center component element {} of {} component on the {}")
    public void center(String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.center(element,elementName,pageName);
    }

    @Given("Click towards component element {} of {} component on the {}")
    public void clickTowards(String elementName, String componentName, String pageName) {
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.clickTowards(element, elementName, pageName);
    }

        @Given("Perform a JS click on element {} of {} component on the {}")
    public void performJSClick(String elementName, String pageName){
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.center(element, elementName, pageName);
        interact.performJSClick(element, elementName, pageName);
    }

    @Given("Perform a JS click on component element {} of {} component on the {}")
    public void performJSClick(String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.center(element, elementName, pageName);
        interact.performJSClick(element, elementName, pageName);
    }

    @Given("If present, click the {} on the {}")
    public void clickIfPresent(String elementName, String pageName){
        try {
            WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
            if (elementIs(element, ElementState.DISPLAYED)) {
                interact.clickStep(element, elementName, pageName);
            }
        }
        catch (WebDriverException ignored){log.new Warning("The " + elementName + " was not present");}
    }

    @Given("If present, click component element {} of {} component on the {}")
    public void clickIfPresent(String elementName, String componentName, String pageName){
        try {
            WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
            if (elementIs(element, ElementState.DISPLAYED)) {
                interact.clickStep(element, elementName, pageName);
            }
        }
        catch (WebDriverException ignored){log.new Warning("The " + elementName + " was not present");}
    }

    @Given("Click listed element {} from {} list on the {}")
    public void clickListedButton(String elementName, String listName, String pageName){
        WebElement element = acquire.listedElementFromPage(elementName,listName,pageName, new ObjectRepository());
        interact.clickStep(element, elementName, pageName);
    }

    @Given("Click listed component element {} of {} from {} list on the {}")
    public void clickListedButton(String elementName, String componentName, String listName, String pageName){
        WebElement element = acquire.listedElementFromComponent(
                elementName,
                componentName,
                listName,
                pageName,
                new ObjectRepository()
        );
        interact.clickStep(element, elementName, pageName);
    }

    @Given("Select component named {} from {} component list on the {} and click the {} element")
    public void clickButtonAmongstComponents(String componentName, String listName, String pageName, String elementName){
        WebElement element = acquire.listedComponentElement(elementName, componentName, listName, pageName, new ObjectRepository());
        interact.clickStep(element, elementName, pageName);
    }

    @Given("Select exact component named {} from {} component list on the {} and click the {} element")
    public void clickButtonAmongstExactNamedComponents(String componentName, String listName, String pageName, String elementName){
        WebElement element = acquire.exactNamedListedComponentElement(elementName,
                componentName,
                listName,
                pageName,
                new ObjectRepository());
        interact.clickStep(element, elementName, pageName);
    }

    @Given("Select component named {} from {} component list on the {} and click listed element {} of {}")
    public void clickListedButtonAmongstComponents(
            String componentName,
            String componentListName,
            String pageName,
            String elementName,
            String elementListName) {
        WebElement element = acquire.listedElementAmongstListedComponents(elementName, elementListName, componentName, componentListName, pageName, new ObjectRepository());
        interact.clickStep(element, elementName, pageName);
    }

    @Given("Click listed attribute element that has {} value for its {} attribute from {} list on the {}")
    public void clickListedButtonByAttribute(String attributeValue, String attributeName, String listName, String pageName) {
        WebElement element = acquire.listedElementByAttribute(attributeName, attributeValue, listName, pageName, new ObjectRepository());
        interact.clickStep(element, attributeName + " attribute named element" , pageName);
    }

    @Given("Click listed attribute element of {} component that has {} value for its {} attribute from {} list on the {}")
    public void clickListedButtonByAttribute(String componentName, String attributeValue, String attributeName, String listName, String pageName) {
        WebElement element = acquire.listedComponentElementByAttribute(componentName, attributeName, attributeValue, listName, pageName, new ObjectRepository());
        interact.clickStep(element, attributeName + " attribute named element" , pageName);
    }

    @Given("Fill listed input {} from {} list on the {} with text: {}")
    public void fillListedInput(String inputName, String listName, String pageName, String input){
        WebElement inputElement = acquire.listedElementFromPage(inputName, listName, pageName, new ObjectRepository());
        interact.basicFill(inputElement, inputName, pageName, input);
    }

    @Given("Fill component input {} of {} component on the {} with text: {}")
    public void fill(String inputName, String componentName, String pageName, String input){
        WebElement inputElement = acquire.elementFromComponent(inputName, componentName, pageName, new ObjectRepository());
        interact.basicFill(inputElement, inputName, pageName, input);
    }

    @Given("Fill component form input on the {}") //TODO: check method, check elementList() log
    public void fillForm(String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = acquire.elementList(table.asMaps(), pageName, new ObjectRepository());
        for (Bundle<WebElement, String, String> form: signForms) interact.basicFill(form.alpha(), form.beta(), pageName, form.theta());

        //List<Map<String, String>> signForms = table.asMaps();
        //String inputName;
        //String input;
        //for (Map<String, String> form : signForms) {
        //    inputName = form.get("Input Element");
        //    input = strUtils.contextCheck(form.get("Input"));
        //    log.new Info("Filling " +
        //            highlighted(BLUE, inputName) +
        //            highlighted(GRAY," on the ") +
        //            highlighted(BLUE, pageName) +
        //            highlighted(GRAY, " with the text: ") +
        //            highlighted(BLUE, input)
        //    );
        //    pageName = strUtils.firstLetterDeCapped(pageName);
        //    clearFillInput(getElementFromPage(inputName, pageName, new ObjectRepository()), //Element
        //            input,
        //            false,
        //            true
        //    );
        //}
    }

    @Given("Fill component form input of {} component on the {}") //TODO: check method (Cannot invoke "Object.getClass()" because "inputClass" is null)
    public void fillForm(String componentName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = acquire.elementList(table.asMaps(), pageName, new ObjectRepository()); //TODO check method log
        for (Bundle<WebElement, String, String> form: signForms) {
            interact.basicFill(form.alpha(), form.theta(), pageName, form.beta());
        }

        //List<Map<String, String>> forms = table.asMaps();
        //String inputName;
        //String input;
        //for (Map<String, String> form : forms) {
        //    inputName = form.get("Input Element");
        //    input = strUtils.contextCheck(form.get("Input"));
        //   log.new Info("Filling " +
        //          highlighted(BLUE, inputName) +
        //          highlighted(GRAY," on the ") +
        //          highlighted(BLUE, pageName) +
        //          highlighted(GRAY, " with the text: ") +
        //          highlighted(BLUE, input)
        //  );
        //  pageName = strUtils.firstLetterDeCapped(pageName);
        //  componentName = strUtils.firstLetterDeCapped(componentName);
        //  clearFillInput(
        //          getElementFromComponent(inputName, componentName, pageName, new ObjectRepository()), //Input element
        //          input,
        //          false,
        //          true
        //  );
        //}
    }

    @Given("Fill iFrame element {} of {} on the {} with text: {}")
    public void fillIframeInput(String inputName,String iframeName,String pageName, String inputText){

        //log.new Info("Filling " +
        //      highlighted(BLUE, inputName) +
        //      highlighted(GRAY," i-frame element input on the ") +
        //      highlighted(BLUE, pageName) +
        //      highlighted(GRAY, " with the text: ") +
        //      highlighted(BLUE, inputText)
        //);

        WebElement iframe = acquire.elementFromPage(inputName, pageName, new ObjectRepository());
        elementIs(iframe, ElementState.DISPLAYED);
        driver.switchTo().frame(iframe); //TODO Inner switchToiFrame() method may be needed
        WebElement element = acquire.elementFromPage(inputName, pageName, new ObjectRepository());
        interact.basicFill(element, inputName, pageName, inputText);
        driver.switchTo().parentFrame();
    }

    @Given("Click i-frame element {} in {} on the {}")
    public void clickIframeElement(String elementName,String iframeName,String pageName ){
        //log.new Info("Clicking the i-frame element " +
        //      highlighted(BLUE, elementName) +
        //      highlighted(GRAY," on the ") +
        //      highlighted(BLUE, pageName)
        //);

        WebElement iframe = acquire.elementFromPage(iframeName, pageName, new ObjectRepository());
        driver.switchTo().frame(iframe); //TODO Pickleib switchToiFrame() method may be needed
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.clickStep(element, elementName, pageName);
    }

    @Given("Fill iframe component form input of {} component on the {}")
    public void fillFormIframe(String iframeName, String componentName, String pageName, DataTable table){
        List<Map<String, String>> forms = table.asMaps();
        String inputName;
        String input;
        for (Map<String, String> form : forms) {
            inputName = form.get("Input Element");
            input = strUtils.contextCheck(form.get("Input"));
            WebElement element = acquire.elementFromPage(iframeName, pageName, new ObjectRepository());
            driver.switchTo().frame(element);
            WebElement inputElement = acquire.elementFromComponent(inputName, componentName, pageName, new ObjectRepository());
           interact.basicFill(inputElement, inputName, pageName, input);
        }
    }

    @Given("Fill listed component input {} of {} component on the {} with text: {}")
    public void fillListedInput(String inputName, String listName, String componentName, String pageName, String input){
        WebElement element = acquire.listedComponentElement(inputName, componentName, listName, pageName, new ObjectRepository());
        interact.basicFill(element, inputName, pageName, input);
    }

    @Given("Verify the text of {} on the {} to be: {}")
    public void verifyText(String elementName, String pageName, String expectedText){
        WebElement element  = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        pageName = strUtils.firstLetterDeCapped(pageName);
        interact.verifyText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of element list {} on the {}") //TODO check
    public void verifyListedText(String listName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = acquire.elementList(table.asMaps(), pageName, new ObjectRepository());
        for (Bundle<WebElement, String, String> form: signForms) {
            WebElement element = acquire.listedElementFromPage(form.beta(), listName, pageName, new ObjectRepository());
            interact.basicFill(element, form.beta(), pageName, form.theta());
        }

        //List<Map<String, String>> signForms = table.asMaps();
        //String elementName;
        //String expectedText;
        //for (Map<String, String> form : signForms) {
        //  elementName = form.get("Input Element");
        //  expectedText = strUtils.contextCheck(form.get("Input"));
        //  log.new Info("Performing text verification for " +
        //          highlighted(BLUE, elementName) +
        //          highlighted(GRAY," on the ") +
        //          highlighted(BLUE, pageName) +
        //          highlighted(GRAY, " with the text: ") +
        //          highlighted(BLUE, expectedText)
        //  );
        //  pageName = strUtils.firstLetterDeCapped(pageName);
        //  List<WebElement> elements = getElementsFromPage(listName, pageName, new ObjectRepository());
        //  WebElement element = acquireNamedElementAmongst(elements, elementName);
        //  Assert.assertEquals("The " + element.getText() + " does not contain text '",expectedText, element.getText());
        //  log.new Success("Text of the element" + element.getText() + " was verified!");
        //
        //}
    }

    @Given("Verify text of the component element {} of {} on the {} to be: {}")
    public void verifyText(String elementName, String componentName, String pageName, String expectedText){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        elementIs(element, ElementState.DISPLAYED);
        interact.center(element, elementName, pageName);
        interact.verifyText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of component element list {} of {} on the {}") //TODO check
    public void verifyListedText(String listName,String componentName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = acquire.elementList(table.asMaps(), pageName, new ObjectRepository());
        interact.verifyListedText(signForms, pageName);

        //List<Map<String, String>> forms = table.asMaps();
        //String elementName;
        //String expectedText;
        //for (Map<String, String> form : forms) {
        //  elementName = form.get("Input Element");
        //  expectedText = strUtils.contextCheck(form.get("Input"));
        //  log.new Info("Performing text verification for " +
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

    @Given("Verify presence of element {} on the {}")
    public void verifyPresence(String elementName, String pageName){
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.verifyState(element, elementName, pageName, ElementState.DISPLAYED);
    }

    @Given("Verify presence of the component element {} of {} on the {}")
    public void verifyPresence(String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.verifyState(element, elementName, pageName, ElementState.DISPLAYED);
    }

    @Given("Checking the presence of the element text on the {}") //TODO check
    public void verifyPresenceText(String pageName, DataTable table) {
        List<Bundle<WebElement, String, String>> elements = acquire.elementList(table.asMaps(), pageName, new ObjectRepository());
        for (Bundle<WebElement, String, String> element: elements) {
            WebElement targetElement = acquire.elementFromPage(element.beta(), pageName, new ObjectRepository());
            interact.verifyState(targetElement, element.beta(), pageName, ElementState.ENABLED);

        }

        //String elementText;
        //List<Map<String, String>> signForms = table.asMaps();
        //List<Map<String, String>> signForms = table.asMaps();
        //for (Map<String, String> form : signForms) {
        //  elementText = strUtils.contextCheck(form.get("Input"));
        //  log.new Info("Performing text verification for " +
        //          highlighted(BLUE, elementText) +
        //          highlighted(GRAY, " on the ") +
        //          highlighted(BLUE, pageName)
        //  );
        //
        //  WebElement element = getElementContainingText(elementText);
        //  verifyElementState(element, ElementState.ENABLED);
        //  log.new Success("Presence of the element text " + elementText + " was verified!");
        //}
    }

    @Given("Close the browser")
    public void closeBrowser(){
        driver.quit();
    }

    @Given("Verify that element {} on the {} is in {} state")
    public void verifyState(String elementName, String pageName, String expectedState){
        expectedState = expectedState.toUpperCase();
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.verifyState(element,elementName, pageName, ElementState.valueOf(expectedState));
    }

    @Given("Verify that component element {} of {} on the {} is in {} state")
    public void verifyState(String elementName, String componentName, String pageName, String expectedState){
        expectedState = expectedState.toUpperCase();
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.verifyState(element,elementName, pageName, ElementState.valueOf(expectedState));
    }

    @Given("If present, verify that component element {} of {} on the {} is in {} state")
    public void verifyIfPresentElement(String elementName, String componentName, String pageName, String expectedState){
        try {
            WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
            if (elementIs(element, ElementState.DISPLAYED)) interact.verifyState(element,elementName, pageName, ElementState.valueOf(expectedState));
        }
        catch (WebDriverException ignored){log.new Warning("The " + elementName + " was not present");}
    }

    @Given("Wait for absence of element {} on the {}")
    public void waitUntilAbsence(String elementName, String pageName){
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        elementIs(element, ElementState.ABSENT); //TODO Inner elementIs() method may be better
    }

    @Given("Wait for absence of component element {} of {} on the {}")
    public void waitUntilAbsence(String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        log.new Info("Waiting for the absence of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        elementIs(element, ElementState.ABSENT);
    }

    @Given("Wait for element {} on the {} to be visible")
    public void waitUntilVisible(String elementName, String pageName){
        WebElement element = acquire.elementFromPage(elementName,pageName, new ObjectRepository());
        log.new Info("Waiting for the visibility of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        elementIs(element, ElementState.DISPLAYED);
    }

    @Given("Wait for component element {} of {} on the {} to be visible")
    public void waitUntilVisible(String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        log.new Info("Waiting for the visibility of " +
                highlighted(BLUE, elementName) +
                highlighted(GRAY," on the ") +
                highlighted(BLUE, pageName)
        );
        elementIs(element, ElementState.DISPLAYED);
    }

    @Given("Wait until element {} on the {} has {} value for its {} attribute")
    public void waitUntilElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.elementFromPage(elementName,pageName, new ObjectRepository());
        //log.new Info("Waiting for the presence of " +
        //      highlighted(BLUE, attributeName) +
        //      highlighted(GRAY, " attribute of ") +
        //      highlighted(BLUE, elementName) +
        //      highlighted(GRAY," on the ") +
        //      highlighted(BLUE, pageName)
        //); // TODO check log if needed
        try {interact.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);}
        catch (WebDriverException ignored) {}
    }

    @Given("Wait until component element {} of {} on the {} has {} value for its {} attribute")
    public void waitUntilElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.elementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        try {interact.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);}
        catch (WebDriverException ignored) {}
    }

    @Given("Verify that element {} on the {} has {} value for its {} attribute")
    public void verifyElementContainsAttribute(
            String elementName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.elementFromPage(elementName,pageName, new ObjectRepository());
        interact.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    @Given("Verify {} css attribute of element {} on the {} is {} ")
    public void verifyElementColor(
            String attributeName,
            String elementName,
            String pageName,
            String attributeValue) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.elementFromPage(elementName,pageName, new ObjectRepository());
       interact.verifyElementColor(element, attributeName, elementName, pageName, attributeValue);
    }

    @Given("Verify that component element {} of {} on the {} has {} value for its {} attribute")
    public void verifyElementContainsAttribute(
            String elementName,
            String componentName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.elementFromComponent(elementName, componentName,pageName, new ObjectRepository());
        interact.verifyElementContainsAttribute(element, elementName ,pageName, attributeName, attributeValue);
    }

    @Given("Select component by {} named {} from {} component list on the {} and verify that it has {} value for its {} attribute")
    public void verifySelectedComponentContainsAttribute(
            String elementFieldName,
            String componentName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        pageName = strUtils.firstLetterDeCapped(pageName);
        listName = strUtils.firstLetterDeCapped(listName);

        acquire.listedComponentElementByAttribute(componentName, attributeValue, attributeName, listName, pageName, new ObjectRepository());
        //TODO acquireExactNamedComponentAmongst() needed?

        List<WebComponent> components = getComponentsFromPage(listName, pageName, new ObjectRepository());
        WebComponent component = acquireExactNamedComponentAmongst(components, componentName, elementFieldName);
        log.new Info("Verifying " +
                highlighted(BLUE, attributeName) +
                highlighted(GRAY, " attribute of ") +
                highlighted(BLUE, componentName) +
                highlighted(GRAY," component on the ") +
                highlighted(BLUE, pageName)
        );
        Assert.assertTrue(
                "The " + attributeName + " attribute of element " + componentName + " could not be verified." +
                        "\nExpected value: " + attributeValue + "\nActual value: " + component.getAttribute(attributeName),
                wait.until(ExpectedConditions.attributeContains(component, attributeName, attributeValue))
        );
        log.new Success("Value of '" + attributeName + "' attribute is verified to be '" + attributeValue + "'!");
    }

    @Given("Select component named {} from {} component list on the {} and verify that the {} element has {} value for its {} attribute") //TODO check
    public void verifySelectedComponentElementContainsAttribute(
            String componentName,
            String listName,
            String pageName,
            String elementName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.listedComponentElement(elementName, componentName, listName, pageName, new ObjectRepository());
        interact.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);

        //pageName = strUtils.firstLetterDeCapped(pageName);
        //listName = strUtils.firstLetterDeCapped(listName);
        //WebElement element = getElementAmongstComponentsFromPage(
        //      elementName,
        //      componentName,
        //      listName,
        //      pageName,
        //      new ObjectRepository()
        //);
        //log.new Info("Verifying " +
        //      highlighted(BLUE, attributeName) +
        //      highlighted(GRAY, " attribute of ") +
        //      highlighted(BLUE, componentName) +
        //      highlighted(GRAY," on the ") +
        //      highlighted(BLUE, pageName)
        //);
        //Assert.assertTrue(
        //      "The " + attributeName + " attribute of element " + componentName + " could not be verified." +
        //              "\nExpected value: " + attributeValue + "\nActual value: " + element.getAttribute(attributeName),
        //      wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue))
        //);
        //log.new Success("Value of '" + attributeName + "' attribute is verified to be '" + attributeValue + "'!");
    }

    @Given("Select component named {} from {} component list on the {} and verify listed element {} of {} has {} value for its {} attribute") //TODO check
    public void verifySelectedComponentContainsAttribute(
            String componentName,
            String componentListName,
            String pageName,
            String elementName,
            String elementListName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.listedElementAmongstListedComponents(elementName, elementListName, componentName, componentListName, pageName, new ObjectRepository());
        interact.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);

        //elementName = strUtils.contextCheck(elementName);
        //componentName = strUtils.contextCheck(componentName);
        //pageName = strUtils.firstLetterDeCapped(pageName);
        //componentListName = strUtils.firstLetterDeCapped(componentListName);
        //List<WebComponent> components = getComponentsFromPage(componentListName, pageName, new ObjectRepository());
        // WebComponent component = acquireNamedComponentAmongst(components, componentName);
        // List<WebElement> elements = getElementsFromComponent(elementListName, component);
        // WebElement element = acquireNamedElementAmongst(elements, elementName);
        //log.new Info("Verifying " +
        //         highlighted(BLUE, attributeName) +
        //      highlighted(GRAY, " attribute of ") +
        //      highlighted(BLUE, componentName) +
        //      highlighted(GRAY," on the ") +
        //      highlighted(BLUE, pageName)
        //);
        //Assert.assertTrue(
        //      "The " + attributeName + " attribute of element " + componentName + " could not be verified." +
        //              "\nExpected value: " + attributeValue + "\nActual value: " + element.getAttribute(attributeName),
        //      wait.until(ExpectedConditions.attributeContains(element, attributeName, attributeValue))
        //);
        //log.new Success("Value of '" + attributeName + "' attribute is verified to be '" + attributeValue + "'!");
    }

    @Given("Verify that element {} from {} list on the {} has {} value for its {} attribute")
    public void verifyListedElementContainsAttribute(
            String elementName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.listedElementFromPage(elementName, listName, pageName, new ObjectRepository());
        interact.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    @Given("Verify text of listed element {} from the {} on the {} is equal to {}")
    public void verifyListedElementContainsText(
            String elementName,
            String listName,
            String pageName,
            String expectedText) {
        WebElement element = acquire.listedElementFromPage(elementName, listName, pageName, new ObjectRepository());
       interact.verifyContainsText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of listed element from the {} on the {}") //TODO check
    public void verifyListedElementContainsText(String listName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = acquire.elementList(table.asMaps(), pageName, new ObjectRepository());
        for (Bundle<WebElement, String, String> form : signForms) {
            WebElement element = acquire.elementFromPage(form.beta(), pageName, new ObjectRepository());
            interact.verifyContainsText(element, form.beta(), pageName, form.theta());
        }

        //List<Map<String, String>> signForms = table.asMaps();
        //String elementName;
        //String expectedText;
        //for (Map<String, String> form : signForms) {
        //  elementName = form.get("Input Element");
        //  expectedText = strUtils.contextCheck(form.get("Input"));
        //  pageName = strUtils.firstLetterDeCapped(pageName);
        //  List<WebElement> elements = getElementsFromPage(listName, pageName, new ObjectRepository());
        //  WebElement element = acquireNamedElementAmongst(elements, elementName);
        //  log.new Info("Performing text verification for " +
        //          highlighted(BLUE, elementName) +
        //          highlighted(GRAY," on the ") +
        //          highlighted(BLUE, pageName) +
        //          highlighted(GRAY, " with the text: ") +
        //          highlighted(BLUE, expectedText)
        //  );
        //  Assert.assertTrue(
        //          "The " + elementName + " does not contain text '" + expectedText + "' ",
        //          element.getText().contains(expectedText)
        //  );
        //  log.new Success("Text of '" + elementName + "' verified as '" + expectedText + "'!");
        //}
    }

    @Given("Verify text of listed component element {} from the {} of {} on the {} is equal to {}")
    public void verifyListedComponentElementContainsText(
            String elementName,
            String listName,
            String componentName,
            String pageName,
            String expectedText) {
        WebElement element = acquire.listedElementFromComponent(elementName, componentName, listName, pageName, new ObjectRepository());
        interact.verifyContainsText(element, elementName, pageName, expectedText);
    }

    @Given("Verify text of listed component element from the {} of {} on the {}") //TODO check
    public void verifyListedComponentElementContainsText(String listName, String componentName, String pageName, DataTable table){
        List<Bundle<WebElement, String, String>> signForms = acquire.elementList(table.asMaps(), pageName, new ObjectRepository());
        for (Bundle<WebElement, String, String> form : signForms) {
            WebElement element = acquire.elementFromComponent(form.beta(), componentName, pageName, new ObjectRepository());
            interact.verifyContainsText(element, form.beta(), pageName, form.theta());
        }

        //List<Map<String, String>> signForms = table.asMaps();
        //String elementName;
        //String expectedText;
        //for (Map<String, String> form : signForms) {
        //  elementName = form.get("Input Element");
        //  expectedText = strUtils.contextCheck(form.get("Input"));
        //  pageName = strUtils.firstLetterDeCapped(pageName);
        //  componentName = strUtils.firstLetterDeCapped(componentName);
        //  List<WebElement> elements = getElementsFromComponent(listName, componentName, pageName, new ObjectRepository());
        //  WebElement element = acquireNamedElementAmongst(elements, elementName);
        //  log.new Info("Performing text verification for " +
        //          highlighted(BLUE, elementName) +
        //          highlighted(GRAY, " on the ") +
        //          highlighted(BLUE, pageName) +
        //          highlighted(GRAY, " with the text: ") +
        //          highlighted(BLUE, expectedText)
        //  );
        //  Assert.assertTrue(
        //          "The " + elementName + " does not contain text '" + expectedText + "' ",
        //          element.getText().contains(expectedText)
        //  );
        //  log.new Success("Text of '" + elementName + "' verified as '" + expectedText + "'!");
        //}
    }

    @Given("Verify presence of listed component element {} of {} from {} list on the {}") //TODO weird methos, check
    public void verifyListedComponentElementContainsText(String elementText, String listName, String componentName, String pageName){
        WebElement element = acquire.listedElementFromComponent(elementText, componentName, listName, pageName, new ObjectRepository());
        interact.verifyPresence(element, elementText, pageName);

        //elementText = strUtils.contextCheck(elementText);
        // pageName = strUtils.firstLetterDeCapped(pageName);
        //componentName = strUtils.firstLetterDeCapped(componentName);
        //List<WebElement> elements = getElementsFromComponent(listName, componentName, pageName, new ObjectRepository());
        //WebElement element = acquireNamedElementAmongst(elements, elementText);
        //log.new Info("Performing text verification for " +
        //      highlighted(BLUE, elementText) +
        //      highlighted(GRAY, " on the ") +
        //      highlighted(BLUE, pageName) +
        //      highlighted(GRAY, " with the text: ") +
        //      highlighted(BLUE, elementText)
        //);
        //Assert.assertTrue(
        //      "The " + elementText + " does not contain text '" + elementText + "' ",
        //      element.getText().contains(elementText)
        //);
        //log.new Success("Text of '" + elementText + "' verified as '" + elementText + "'!");
    }

    @Given("Verify that component element {} of {} from {} list on the {} has {} value for its {} attribute")
    public void verifyListedElementContainsAttribute(
            String elementName,
            String componentName,
            String listName,
            String pageName,
            String attributeValue,
            String attributeName) {
        attributeValue = strUtils.contextCheck(attributeValue); //TODO should we transfer it to verifyElementContainsAttribute()?
        WebElement element = acquire.listedElementFromComponent(elementName, componentName, listName, pageName, new ObjectRepository());
        interact.verifyElementContainsAttribute(element, elementName, pageName, attributeName, attributeValue);
    }

    @Given("Acquire & save email with {} -> {}") //TODO email utility?
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

    @Given("Verify the page is redirecting to the page {}")
    public void verifyCurrentUrl(String url) {
        interact.verifyCurrentUrl(url);
    }

    @Given("Verify the url contains with the text {}")
    public void verifyTextUrl(String text) {
        interact.verifyCurrentUrl(text); //TODO universal method log may be better
        //log.new Info("The url contains " + text);
        //Assert.assertTrue("The page is not directed to: " + text ,driver.getCurrentUrl().contains(text));
    }

    @Given("Click the specific text {} button")
    public void clickButtonWithText(String buttonText, Boolean scroll) {
        interact.clickButtonByText(buttonText, scroll);
    }

    @Given("Verify the booking email on the myReservation page to be {}")
    public void checkEmail(String buttonText, Boolean scroll) {
        interact.clickButtonByText(buttonText, scroll);
    }

    @Given("Update context {} -> {}")
    public void updateContext(String key, String value){
        ContextStore.put(key, strUtils.contextCheck(value));
    }

    @Given("Clear component input field {} from {} component on the {}")
    public void clearInputField(String elementName, String componentName, String pageName){
        WebElement element =  acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        element.clear(); // TODO Inner clear() method may be needed
    }

    @Given("Press {} key on {} element of the {}")
    public void pressKey(Keys key, String elementName, String pageName){
        WebElement element = acquire.elementFromPage(elementName, pageName, new ObjectRepository());
        interact.pressKey(element, key, elementName, pageName);
    }

    @Given("Press {} key on component element {} from {} component on the {}")
    public void pressKey(Keys key, String elementName, String componentName, String pageName){
        WebElement element = acquire.elementFromComponent(elementName, componentName, pageName, new ObjectRepository());
        interact.pressKey(element, key, elementName, pageName);
    }

    @Given("Execute JS command: {}")
    public void executeJSCommand(String script) {
        interact.executeJSCommand(script);
    }

    @Given("Listen to {} event & print {} object") //TODO check
    public void listenGetAndPrintObject(String eventName, String objectScript)  {
        String listenerScript = "_ddm.listen(" + eventName + ");";
        interact.listenGetAndPrintObject(listenerScript, eventName, objectScript);
    }

    @Given("Upload file on component input {} of {} component on the {} with file: {}")
    public void fillInputWithFile(String inputName, String componentName, String pageName, String path){
        WebElement inputElement = acquire.elementFromComponent(inputName, componentName, pageName, new ObjectRepository());
        interact.fillInputWithFile(inputElement, inputName, pageName, path);
    }
}