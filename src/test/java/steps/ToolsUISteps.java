package steps;

import io.cucumber.java.en.*;
import pages.ElementsPage;
import pages.ToolsUI;

public class ToolsUISteps {

    ToolsUI toolsUI = new ToolsUI();

    ElementsPage elementsPage = new ElementsPage();

    @Given("Click tool named {}")
    public void clickToolNamed(String toolName) {toolsUI.selectToolNamed(toolName);}

    @Given("Click tool type named {}")
    public void clickToolTypeNamed(String toolName) {toolsUI.selectToolTypeNamed(toolName);}

    @Given("Fill name input with {}")
    public void fillNameInput(String name) {elementsPage.fillNameInput(name);}

    @Given("Fill email input with {}")
    public void fillEmailInput(String email) {elementsPage.fillEmailInput(email);}

    @Given("Fill current address input with {}")
    public void fillCurrentAddressInput(String currentAddress) {elementsPage.fillCurrentAddressInput(currentAddress);}

    @Given("Fill permanent address input with {}")
    public void fillPermanentAddressInput(String permanentAddress) {elementsPage.fillPermanentAddressInput(permanentAddress);}

    @Given("Click submit button")
    public void clickSubmit() {elementsPage.clickSubmit();}
}
