package common;

import lombok.Getter;
import pages.*;
import pages.inventory.app.*;
import pickleib.utilities.interfaces.repository.PageRepository;
import uwp.BasicUWPMainPage;

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

    //Pages
    BasicUWPMainPage basicUWPMainPage;
    PracticeFormPage practiceFormPage;
    BookDetailsPage bookDetailsPage;
    BookStorePage bookStorePage;
    Transaction transaction;
    LandingPage landingPage;
    ProfilePage profilePage;
    Categories categories;
    LoginFrame loginFrame;
    ToolsPage toolsPage;
    LoginPage loginPage;
    Dashboard dashboard;
    Products products;
    Sales sales;

    //Screens
    InventoryManagementFrame inventoryManagementFrame;
}
