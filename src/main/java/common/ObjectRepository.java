package common;

import lombok.Getter;
import pages.LandingPage;
import pages.ToolsPage;

@SuppressWarnings("unused")
public class ObjectRepository {

    @Getter
    public enum Environment {
        test("test-url"),
        acceptance("acc-url");

        final String urlKey;

        Environment(String urlKey){
            this.urlKey = urlKey;
        }
    }    public static Environment environment;

    LandingPage landingPage = new LandingPage();
    ToolsPage toolsPage = new ToolsPage();
}
