package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

public class GoogleCurrencyContainer extends WebUtilities {

    @FindBy(css = "[id='knowledge-currency__updatable-data-column']")
    public WebElement dataColumn;

    @FindBy(css = "[data-exchange-rate]")
    public WebElement exchangeRate;//

    @FindBy(css = "[data-value]")
    public WebElement rateValue;

    public String getRateText(){return exchangeRate.getText();}

    public Double getRate(){return Double.parseDouble(rateValue.getText().replace(",","."));}

}
