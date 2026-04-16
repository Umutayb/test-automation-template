package steps;

import context.ContextStore;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import pickleib.platform.driver.PickleibAppiumDriver;
import pickleib.platform.driver.ServiceFactory;
import pickleib.utilities.screenshot.ScreenCaptureUtility;
import pickleib.web.PickleibPageObject;
import pickleib.web.driver.PickleibWebDriver;
import pickleib.web.driver.WebDriverFactory;
import utils.Printer;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import static utils.StringUtilities.Color.PURPLE;
import static utils.StringUtilities.highlighted;

public class Hooks extends PickleibPageObject {

    @Getter
    public enum Environment {
        test("test-url"),
        dev("dev-url"),
        acceptance("acc-url");

        final String urlKey;

        Environment(String urlKey){
            this.urlKey = urlKey;
        }

        public String getUrlKey() {
            return urlKey;
        }
    }

    public static Environment environment;

    public Scenario scenario;
    public static boolean initialiseBrowser;
    public static boolean initialiseAppiumDriver;

    Printer log = new Printer(this.getClass());


    @SuppressWarnings("unused")
    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object transformer(Object fromValue, Type toValueType) {
        return objectMapper.convertValue(fromValue, objectMapper.constructType(toValueType));
    }

    @Given("^Navigate to the (acceptance|test|dev) page$")
    public void navigateToTargetEnv(Environment environment) {
        String baseUrl = ContextStore.get(environment.getUrlKey());
        String url = baseUrl.startsWith("http") ? baseUrl : "https://" + baseUrl;
        log.info("Navigating to " + highlighted(PURPLE, url));
        PickleibWebDriver.get().get(url);
        Hooks.environment = environment;
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
            if (ServiceFactory.service.get() == null) PickleibAppiumDriver.startService();
            PickleibAppiumDriver.initialize();
        }
        Hooks.environment = null;
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
            if (initialiseBrowser)
                PickleibWebDriver.terminate();

            if (initialiseAppiumDriver)
                PickleibAppiumDriver.terminate();

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
        initialiseBrowser = scenario.getSourceTagNames().contains("@Web-UI");
        initialiseAppiumDriver = scenario.getSourceTagNames().contains("@Mobile-UI")
                || scenario.getSourceTagNames().contains("@Desktop-UI");
    }

    public WebDriverFactory.BrowserType getBrowserType(Scenario scenario) {
        for (WebDriverFactory.BrowserType browserType: WebDriverFactory.BrowserType.values()) {
            if (scenario
                    .getSourceTagNames()
                    .stream()
                    .anyMatch(
                            tag -> tag.replaceAll("@", "").equalsIgnoreCase(browserType.name())
                    )
            ) return browserType;
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
