package pages;

import com.google.gson.JsonObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.components.GoogleCurrencyContainer;
import pages.components.GoogleSearchResult;
import utils.EmailUtilities;
import utils.Printer;
import utils.ScreenCaptureUtility;
import utils.WebUtilities;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Google extends WebUtilities {

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

    public void printRate(){log.new Important(currencyContainer.getRateText());}

    public void notifyIfChanged(){
        log.new Info("Checking exchange rate");

        EmailUtilities email = new EmailUtilities();

        String subject = "Exchange rate has changed!";
        StringBuilder content = new StringBuilder();
//        String receiver = "umutaybora@gmail.com";
        JsonObject receivers = jsonUtils.parseJsonFile("src/test/resources/files/receivers","Receivers");
        String password ="Notifier1234";
        String id = "umutayb.notification@gmail.com";
        String lastStatus = currencyContainer.getRateText();
        Double lastUpdate;

        File file = new File("src/test/resources/files/LastExchangeState.txt");

        try(Scanner in = new Scanner(file, StandardCharsets.UTF_8)){
            StringBuilder status = new StringBuilder();
            while(in.hasNext()) {status.append(in.nextLine());}
            in.close();
            lastUpdate = Double.parseDouble(status.toString());
        }
        catch (IOException ignored) {lastUpdate = null;}

        assert lastUpdate != null;
        double delta = numeric.shortenDouble(Math.abs(currencyContainer.getRate() - lastUpdate)/lastUpdate*100);
        try {
            if (delta > 0.8){
                log.new Important("Order status has changed!");
                log.new Important(lastStatus);
                content.append(lastStatus).append("\n");
                subject = subject + " - " + currencyContainer.getRate() + " (Old rate was "+lastUpdate+")";
                BodyPart attachment = new MimeBodyPart();
                BodyPart bodyPart = new MimeBodyPart();
                Multipart body = new MimeMultipart();
                bodyPart.setText(content.toString());
                body.addBodyPart(bodyPart);
                body.addBodyPart(attachment);
                ScreenCaptureUtility capture = new ScreenCaptureUtility();
                File attachmentFile = capture.captureScreen("",driver);
                DataSource source = new FileDataSource(capture.captureScreen("",driver));
                attachment.setDataHandler(new DataHandler(source));
                attachment.setFileName(attachmentFile.getName());
                for (String receiver:receivers.keySet()) {
                    email.sendEmail(
                            subject,
                            content.toString(),
                            String.valueOf(receivers.get(receiver)),
                            id,
                            password,
                            body);
                }
                try (PrintWriter writer = new PrintWriter(file)) {writer.println(currencyContainer.getRate());}
                catch (IOException fileNotFoundException) {fileNotFoundException.printStackTrace();}
            }
            else log.new Info("The change in exchange ratio is "+Math.abs(currencyContainer.getRate() - lastUpdate)/lastUpdate*100+"%!");
        }
        catch (NullPointerException e) {
            try (PrintWriter writer = new PrintWriter(file)) {writer.println(lastStatus);}
            catch (IOException fileNotFoundException) {fileNotFoundException.printStackTrace();}
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
