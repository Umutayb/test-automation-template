package common;

import lombok.Getter;
import pages.LandingPage;
import pages.LoginPage;
import pages.ProfilePage;
import pages.ToolsPage;
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

    LandingPage landingPage;
    ToolsPage toolsPage;
    LoginPage loginPage;
    ProfilePage profilePage;

}
