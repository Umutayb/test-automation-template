package steps;

import common.ObjectRepository;
import context.ContextStore;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import pickleib.steps.BuiltInSteps;
import pickleib.utilities.steps.PickleibSteps;

import static utils.StringUtilities.Color.BLUE;
import static utils.StringUtilities.highlighted;

/**
 * Project-specific step definitions.
 * Generic steps (click, fill, verify, scroll, etc.) are provided by Pickleib's
 * BuiltInSteps — add "pickleib.steps" to your Cucumber glue path.
 *
 * This class only contains steps that are specific to this project's setup,
 * and wires the element repository for BuiltInSteps.
 */
public class CommonSteps extends PickleibSteps {

    public CommonSteps() {
        super(
                ObjectRepository.class,
                Hooks.initialiseAppiumDriver,
                Hooks.initialiseBrowser
        );
    }

    /**
     * Wires the element repository to BuiltInSteps before each scenario.
     * This ensures generic steps from pickleib.steps have access to page objects.
     */
    @Before(order = 0)
    public void wireElementRepository() {
        BuiltInSteps.setElementRepository(getElementRepository());
    }

    /**
     * Navigates to the specified environment page (acceptance, test, dev).
     * Handles URLs with and without protocol prefix.
     */
    @Given("^Navigate to the (acceptance|test|dev) page$")
    public void navigateToTargetEnv(ObjectRepository.Environment environment) {
        String username = ContextStore.get("website-username");
        String password = ContextStore.get("website-password");
        String baseUrl = ContextStore.get(environment.getUrlKey());

        String url;
        if (baseUrl.startsWith("http://") || baseUrl.startsWith("https://")) {
            url = baseUrl;
        } else {
            String protocol = ContextStore.get("protocol", "https").toLowerCase();
            url = protocol + "://" + baseUrl;
            if (ObjectRepository.environment == null && username != null && password != null)
                url = protocol + "://" + username + ":" + password + "@" + baseUrl;
        }

        log.info("Navigating to " + highlighted(BLUE, url));
        webInteractions.driver.get(url);
        ObjectRepository.environment = environment;
    }
}
