package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import requestObject.RequestUserObject;

public class LoginPage extends BasePage{
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "userName")
    private WebElement userNameElement;
    @FindBy(id = "password")
    private WebElement passwordElement;
    @FindBy(id = "login")
    private WebElement loginElement;
    @FindBy(id = "name")
    private WebElement errorMessageElement;

    public void loginIntoApplication(RequestUserObject requestBody){
        elementMethods.fillElement(userNameElement, requestBody.getUserName());
        elementMethods.fillElement(passwordElement, requestBody.getPassword());
        elementMethods.clickJSElement(loginElement);
    }

    public void validateLoginError(){
        String actualError = errorMessageElement.getText();
        Assert.assertEquals(actualError, "Invalid username or password!");
    }
}
