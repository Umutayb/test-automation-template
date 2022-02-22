package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.EmailUtilities;
import utils.Printer;
import utils.WebUtilities;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class UpsHomePage extends WebUtilities {

    Printer log = new Printer(UpsHomePage.class);
    EmailUtilities email = new EmailUtilities();

    @FindBy(css = "modal-shipment-view-details")
    public WebElement detailsButton;

    @FindBy(css = "[data-tab='shipment-progress-tab']")
    public WebElement progressTab;

    @FindBy(css = "[id=\"ctl00_MainContent_DataListSonIslem\"] table")
    public List<WebElement> activities;

    @FindBy(css = "[name='ctl00$text_yurtici_takip']")
    public WebElement domesticPackageTrackingNumberInput;

    @FindBy(css = "[type='submit'][name*='yurtici_takip']")
    public WebElement domesticPackageTrackingNumberSubmitButton;

    public void listActivities(){
        String subject = "Order status has changed!";
        StringBuilder content = new StringBuilder();
        String receiver = "umutaybora@gmail.com";
        String password ="Notifier1234";
        String id = "umutayb.notification@gmail.com";
        String lastStatus = activities.get(1).getText();
        String lastUpdate;

        Scanner scanner = new Scanner(lastStatus);
        File file = new File("src/test/resources/files/LastCargoState.txt");

        try(Scanner in = new Scanner(file, StandardCharsets.UTF_8)){
            StringBuilder status = new StringBuilder();
            while(in.hasNext()) {status.append(in.nextLine());}
            in.close();
            lastUpdate = status.toString();
        }
        catch (IOException ignored) {lastUpdate = null;}

        for (WebElement activity:activities) {
            log.new Info(activity.getText());
            content.append("\n").append(activity.getText()).append("\n");
        }

        while (scanner.hasNext()){
            assert lastUpdate != null;
            String line = scanner.nextLine();
            if (!lastUpdate.contains(line)){
                log.new Important("Order status has changed!");
                log.new Important(lastStatus);
                email.sendEmail(subject,content.toString(),receiver,id,password,null);
                try (PrintWriter writer = new PrintWriter(file)) {writer.println(lastStatus);}
                catch (IOException fileNotFoundException) {fileNotFoundException.printStackTrace();}
                break;
            }
        }
    }
}
