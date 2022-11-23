package common;

import pages.LandingPage;
import pages.ToolsPage;

@SuppressWarnings("unused")
public class ObjectRepository {

    public enum Environment { TEST, ACCEPTANCE}
    public static Environment environment;

    LandingPage landingPage = new LandingPage();
    ToolsPage toolsPage = new ToolsPage();
}
