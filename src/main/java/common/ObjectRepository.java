package common;

import lombok.Getter;
import pages.*;
import pages.inventory.app.*;
import pickleib.utilities.interfaces.repository.PageObjectRepository;
import uwp.BasicUWPMainPage;

@SuppressWarnings("unused")
public class ObjectRepository implements PageObjectRepository {

    @Getter
    public enum Environment {
        test("test-url"),
        dev("dev-url"),
        acceptance("acc-url");

        final String urlKey;

        Environment(String urlKey){
            this.urlKey = urlKey;
        }

        public String getUrlKey() {
            return urlKey;
        }
    }

    public static Environment environment;

    // Web Pages
    HomePage homePage;
    FormsPage formsPage;
    AlertsPage alertsPage;
    ButtonsPage buttonsPage;
    DropdownPage dropdownPage;
    TallPage tallPage;
    ElementsPage elementsPage;
    InteractionsPage interactionsPage;
    RadioButtonsPage radioButtonsPage;
    TextInputsPage textInputsPage;
    CheckboxesPage checkboxesPage;
    ModalPage modalPage;
    LoginFormPage loginFormPage;
    DroppablePage droppablePage;

    // Desktop/Mobile Screens
    BasicUWPMainPage basicUWPMainPage;
    InventoryManagementFrame inventoryManagementFrame;
    Transaction transaction;
    Categories categories;
    LoginFrame loginFrame;
    Dashboard dashboard;
    Products products;
    Sales sales;
}
