package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import requestObject.RequestUserObject;
import java.time.Duration;

public class ProfilePage extends BasePage {
    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "userName-value")
    private WebElement userNameValueElement;
    @FindBy(xpath = "//button[text()='Delete Account']")
    private WebElement deleteAccountElement;
    @FindBy(id = "closeSmallModal-ok")
    private WebElement confirmDeleteOK;

    public void validateLoginProcess(RequestUserObject requestBody) {
        String actualUserName = userNameValueElement.getText();
        Assert.assertEquals(actualUserName, requestBody.getUserName());
    }

    public void deleteAccount() {

        elementMethods.clickJSElement(deleteAccountElement);
        elementMethods.clickJSElement(confirmDeleteOK);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.alertIsPresent());

        Alert accountAlert = driver.switchTo().alert();
        System.out.println(accountAlert.getText());
        accountAlert.accept();
    }
}
