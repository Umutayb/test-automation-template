package steps;

import io.cucumber.java.en.Given;
import pages.demoqa.ProfilePage;

public class DemoQASteps {
    ProfilePage profilePage = new ProfilePage();

    @Given("Print book attribute")
    public void printAttribute(){
        profilePage.getRowAttribute("Git Pocket Guide","");
    }
}
