package pages;

import com.google.gson.JsonObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.components.GoogleCurrencyContainer;
import pages.components.GoogleSearchResult;
import utils.EmailUtilities;
import utils.Printer;
import utils.Utilities;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Google extends Utilities {

    Printer log = new Printer(Google.class);

    @FindBy(css = "[name='q']")
    public WebElement searchInput;

    @FindBy(css = "[class='obcontainer']")
    public GoogleCurrencyContainer currencyContainer;

    @FindBy(css = "[class='g']")
    public List<GoogleSearchResult> results;

    @FindBy(css = "button[id='L2AGLb'] [role='none']")
    public WebElement cookieAcceptButton;

    public void searchFor(String input){
        clearFillInput(searchInput,input,true);
        searchInput.sendKeys(Keys.ENTER);
    }

    public void printRate(){log.new important(currencyContainer.getRateText());}

    public void notifyIfChanged(){
        EmailUtilities email = new EmailUtilities();

        String subject = "Exchange rate has changed!";
        StringBuilder content = new StringBuilder();
//        String receiver = "umutaybora@gmail.com";
        JsonObject receivers = jsonUtils.parseJsonFile("src/test/resources/files/receivers","Receivers");
        String password ="Notifier1234";
        String id = "umutayb.notification@gmail.com";
        String lastStatus = currencyContainer.getRateText();
        String lastUpdate;

        Scanner scanner = new Scanner(lastStatus);
        File file = new File("src/test/resources/files/LastExchangeState.txt");

        try(Scanner in = new Scanner(file, StandardCharsets.UTF_8)){
            StringBuilder status = new StringBuilder();
            while(in.hasNext()) {status.append(in.nextLine());}
            in.close();
            lastUpdate = status.toString();
        }
        catch (IOException ignored) {lastUpdate = null;}

        while (scanner.hasNext()){
            assert lastUpdate != null;
            String line = scanner.nextLine();
            try {
                if (!lastUpdate.contains(line)){
                    log.new important("Order status has changed!");
                    log.new important(lastStatus);
                    content.append(lastStatus);
                    subject = subject + " - " + currencyContainer.getRate();
                    for (String receiver:receivers.keySet()) {
                        email.sendEmail(subject,content.toString(), String.valueOf(receivers.get(receiver)),id,password);
                    }
                    try (PrintWriter writer = new PrintWriter(file)) {writer.println(lastStatus);}
                    catch (IOException fileNotFoundException) {fileNotFoundException.printStackTrace();}
                    break;
                }
            }
            catch (NullPointerException e) {
                try (PrintWriter writer = new PrintWriter(file)) {writer.println(lastStatus);}
                catch (IOException fileNotFoundException) {fileNotFoundException.printStackTrace();}
                break;
            }
        }
    }
}
