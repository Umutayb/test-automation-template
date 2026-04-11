package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class FileUploadPage {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='file-input-single']")
    public WebElement singleFileInput;

    @FindBy(css = "[data-testid='file-single-name']")
    public WebElement singleFileStatus;

    @FindBy(css = "[data-testid='file-input-multiple']")
    public WebElement multipleFileInput;

    @FindBy(css = "[data-testid='file-multiple-list']")
    public WebElement multipleFileList;

    @FindBy(css = "[data-testid='file-drop-zone']")
    public WebElement dropZone;

    @FindBy(css = "[data-testid='file-drop-list']")
    public WebElement dropList;

    @FindBy(css = "[data-testid='file-drop-clear']")
    public WebElement clearButton;
}
