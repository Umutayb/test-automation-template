package steps;

import io.cucumber.java.en.*;
import pages.ToolsPage;

public class ToolsPageSteps {

    ToolsPage toolsPage = new ToolsPage();

    @Given("Click tool named {}")
    public void clickToolNamed(String toolName) {toolsPage.selectToolNamed(toolName);}

    @Given("Click tool type named {}")
    public void clickToolTypeNamed(String toolName) {toolsPage.selectToolTypeNamed(toolName);}

    @Given("Fill name input with {}")
    public void fillNameInput(String name) {toolsPage.fillNameInput(name);}

    @Given("Fill email input with {}")
    public void fillEmailInput(String email) {toolsPage.fillEmailInput(email);}

    @Given("Fill current address input with {}")
    public void fillCurrentAddressInput(String currentAddress) {toolsPage.fillCurrentAddressInput(currentAddress);}

    @Given("Fill permanent address input with {}")
    public void fillPermanentAddressInput(String permanentAddress) {toolsPage.fillPermanentAddressInput(permanentAddress);}

    @Given("Click submit button")
    public void clickSubmit() {toolsPage.clickSubmit();}
}
