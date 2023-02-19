package common;

import lombok.Getter;
import pages.amazon.HomePage;
import pages.demoqa.LandingPage;
import pages.demoqa.ToolsPage;

@SuppressWarnings("unused")
public class ObjectRepository {

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
}
