package pages.demoqa;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

import java.util.List;

public class LoginPage extends WebUtilities {

    @FindBy(id = "userName")
    WebElement userName;

    @FindBy(id = "password")
    WebElement Password;

    @FindBy(id = "login")
    WebElement loginButton;

    @FindBy(xpath = "//*[@aria-label='rows per page']")
    WebElement rows;

    @FindBy(xpath = "//*[@aria-label='rows per page']//*[@value='10']")
    WebElement rows10;

    public void login(String username, String password) {
        userName.sendKeys(username);
        Password.sendKeys(password);
        loginButton.click();
    }

    public void selectRows10() {
        scrollWithJS(rows);
        rows.click();
        rows10.click();
    }

    public List<WebElement> findElementsTitle() {
        return WebUtilities.driver.findElements(By.xpath("//*[@class='action-buttons']//a"));
    }

    public String getAuthor(int rowNumber) {
        return WebUtilities.driver.findElement(By.xpath("//*[@class='rt-tr-group'][" + rowNumber + "]//div[3]")).getText();
    }

    public String getPublisher(int rowNumber) {
        return WebUtilities.driver.findElement(By.xpath("//*[@class='rt-tr-group'][" + rowNumber + "]//div[4]")).getText();
    }
}
