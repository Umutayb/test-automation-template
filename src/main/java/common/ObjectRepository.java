package common;

import lombok.Getter;
import pages.amazon.HomePage;
import pages.demoqa.LandingPage;
import pages.demoqa.LoginPage;
import pages.demoqa.ProfilePage;
import pages.demoqa.ToolsPage;
import pickleib.utilities.interfaces.repository.PageRepository;

@SuppressWarnings("unused")
public class ObjectRepository implements PageRepository {

    @Getter
    public enum Environment {
        test("test-url"),
        dev("dev-url"),
        acceptance("acc-url");

        final String urlKey;

        Environment(String urlKey){
            this.urlKey = urlKey;
        }
    }

    public static Environment environment;

    LandingPage landingPage = new LandingPage();
    ToolsPage toolsPage = new ToolsPage();
    HomePage homePage = new HomePage();
    LoginPage loginPage = new LoginPage();
    ProfilePage profilePage = new ProfilePage();

}
