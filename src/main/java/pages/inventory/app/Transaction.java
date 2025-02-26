package pages.inventory.app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.mobile.PickleibScreenObject;
import pickleib.mobile.driver.PickleibAppiumDriver;

import java.util.List;

public class Transaction extends PickleibScreenObject {

    @FindBy(xpath = "//*[@Name='Subtotal Row 0']")
    WebElement subtotal;

    @FindBy(xpath = "//*[contains(@Name, 'TransactionId Row')]")
    List<WebElement> transactionIdCells;

    public models.sql.database.model.Transaction get(int index){
        return new models.sql.database.model.Transaction(
                PickleibAppiumDriver.get().findElement(By.xpath("//*[@Name='Date Row " + index + "']")).getText(),
                PickleibAppiumDriver.get().findElement(By.xpath("//*[@Name='Subtotal Row " + index + "']")).getText(),
                PickleibAppiumDriver.get().findElement(By.xpath("//*[@Name='Total Row " + index + "']")).getText(),
                PickleibAppiumDriver.get().findElement(By.xpath("//*[@Name='DiscountPercent Row " + index + "']")).getText(),
                PickleibAppiumDriver.get().findElement(By.xpath("//*[@Name='Change Row " + index + "']")).getText(),
                PickleibAppiumDriver.get().findElement(By.xpath("//*[@Name='DiscountAmount Row " + index + "']")).getText(),
                PickleibAppiumDriver.get().findElement(By.xpath("//*[@Name='TransactionId Row " + index + "']")).getText()
        );
    }

    public models.sql.database.model.Transaction getLast(){
        return get(transactionIdCells.size() -1);
    }
}
