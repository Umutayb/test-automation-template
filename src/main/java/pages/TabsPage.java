package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

public class TabsPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='tab-1']")
    public WebElement overviewTab;

    @FindBy(css = "[data-testid='tab-2']")
    public WebElement detailsTab;

    @FindBy(css = "[data-testid='tab-3']")
    public WebElement codeTab;

    @FindBy(css = "[data-testid='tab-4']")
    public WebElement settingsTab;

    @FindBy(css = "[data-testid='tab-panel-1']")
    public WebElement overviewPanel;

    @FindBy(css = "[data-testid='tab-panel-2']")
    public WebElement detailsPanel;

    @FindBy(css = "[data-testid='tab-panel-3']")
    public WebElement codePanel;

    @FindBy(css = "[data-testid='tab-panel-4']")
    public WebElement settingsPanel;

    @FindBy(css = "[data-testid='tabs-container'] [data-testid^='tab-panel-']:not([style*='display: none'])")
    public WebElement activePanel;
}
