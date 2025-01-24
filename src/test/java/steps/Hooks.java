package steps;

import bookstore.BookStoreAuthorisation;
import bookstore.models.CredentialModel;
import bookstore.models.TokenResponseModel;
import bookstore.models.UserResponseModel;
import common.LogUtility;
import common.ObjectRepository;
import common.PageObject;
import context.ContextStore;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import pickleib.mobile.driver.PickleibAppiumDriver;
import pickleib.mobile.driver.ServiceFactory;
import pickleib.utilities.screenshot.ScreenCaptureUtility;
import pickleib.web.driver.PickleibWebDriver;
import pickleib.web.driver.WebDriverFactory;
import utils.Printer;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import static utils.StringUtilities.Color.PURPLE;
import static utils.StringUtilities.highlighted;

public class Hooks extends PageObject {

    public Scenario scenario;
    public boolean authenticate;
    public static boolean initialiseBrowser;
    public static boolean initialiseAppiumDriver;
    public static boolean isWebUI;
    LogUtility logUtil = new LogUtility();
    Printer log = new Printer(this.getClass());


    public Hooks() {
        ContextStore.loadProperties("src/test/resources/test.properties");
        logUtil.setLogLevel(logUtil.getLogLevel(ContextStore.get("system-log-level", "error")));
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
        log.info("Running: " + highlighted(PURPLE, scenario.getName()));
        processScenarioTags(scenario);

        if (initialiseBrowser) {
            WebDriverFactory.BrowserType browserType = getBrowserType(scenario);
            if (browserType != null) PickleibWebDriver.initialize(browserType);
            else PickleibWebDriver.initialize();
        }
        if (initialiseAppiumDriver) {
            if (ServiceFactory.service == null) PickleibAppiumDriver.startService();
            PickleibAppiumDriver.initialize();
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
        if (initialiseBrowser || initialiseAppiumDriver) {
            String screenshotTag = scenario.getSourceTagNames()
                    .stream()
                    .filter(tag -> tag.contains("SCN-"))
                    .map(tag -> tag.replaceAll("SCN-", ""))
                    .collect(Collectors.joining());
            if (scenario.isFailed()) {
                WebDriver driver = initialiseBrowser ? PickleibWebDriver.get() : PickleibAppiumDriver.get();
                ScreenCaptureUtility.captureScreen(screenshotTag, "png", (RemoteWebDriver) driver);
            }
            if (initialiseBrowser) {
                PickleibWebDriver.terminate();
            }

            if (initialiseAppiumDriver) {
                PickleibAppiumDriver.terminate();
            }
        }
        if (scenario.isFailed()) {
            log.warning(scenario.getName() + ": FAILED!");
        } else {
            log.success(scenario.getName() + ": PASS!");
        }
    }

    public void processScenarioTags(Scenario scenario){
        log.important(scenario.getSourceTagNames().toString());
        this.scenario = scenario;
        authenticate = scenario.getSourceTagNames().contains("@Authenticate");
        initialiseBrowser = scenario.getSourceTagNames().contains("@Web-UI");
    }

    public WebDriverFactory.BrowserType getBrowserType(Scenario scenario) {
        for (WebDriverFactory.BrowserType browserType: WebDriverFactory.BrowserType.values()) {
            if (scenario.getSourceTagNames().stream().anyMatch(tag -> tag.replaceAll("@", "").equalsIgnoreCase(browserType.name())))
                return browserType;
        }
        return null;
    }

    /**
     * Takes a screenshot of the current page.
     */
    @Given("Take a screenshot")
    public void takeAScreenshot() {
        ScreenCaptureUtility.captureScreen(scenario.getName().replaceAll(" ", "_"), "png", PickleibWebDriver.get());
    }
}
