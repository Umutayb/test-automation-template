package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.components.TextBox;
import utils.EmailUtilities;
import utils.Printer;
import utils.Utilities;

import java.util.List;

public class UpsHomePage extends Utilities {

    Printer log = new Printer(UpsHomePage.class);

    EmailUtilities email = new EmailUtilities();

    @FindBy(css = "modal-shipment-view-details")
    public WebElement detailsButton;

    @FindBy(css = "[data-tab='shipment-progress-tab']")
    public WebElement progressTab;

    @FindBy(css = "[id*='stApp_activitydetails']")
    public List<WebElement> activities;

    private final String lastUpdate = "21/10/2021\n" +
            "11:51 Yolda\n" +
            "UPS gümrük bilgileri için alıcı veya ithalatçıyla iletişimi başlattı. Bilgiler alındığında gümrük işlemine gönderilecek. / Alıcıyla irtibata geçtik.\n" +
            "Istanbul, Turkey";

    public void listActivities(){
        String subject = "Order status has changed!";
        StringBuilder content = new StringBuilder();
        String receiver = "umutaybora@gmail.com";
        String password ="Notifier1234";
        String id = "umutayb.notification@gmail.com";

        for (WebElement activity:activities) {
            log.new info(activity.getText());
            content.append("\n").append(activity.getText());
        }
        if (!activities.get(0).getText().equals(lastUpdate)){
            log.new important("Order status has changed!");
            log.new important(activities.get(0).getText());
            email.sendEmail(subject,content.toString(),receiver,id,password);
        }
    }
}
