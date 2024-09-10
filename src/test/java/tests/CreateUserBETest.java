package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;
import pages.LoginPage;
import requestObject.RequestUserObject;
import responseObject.ResponseTokenObject;
import responseObject.ResponseUserObject;
import services.AccountService;

import java.time.Duration;

public class CreateUserBETest {

    public AccountService accountService;
    public RequestUserObject requestBody;
    public WebDriver driver;
    public String userId;
    public String token;

    @Test
    public void testMethod() {
        System.out.println("===== STEP 1: CREATE ACCOUNT =====");
        createAccount();

        System.out.println("===== STEP 2: GENERATE TOKEN =====");
        generateToken();

        System.out.println("===== STEP 3: GET USER DETAILS =====");
        validateAccountBE();

        System.out.println("===== STEP 4: DELETE USER =====");
        deleteAccountBE();

        System.out.println("===== STEP 5: LOGIN USER =====");
        loginApplication();

        System.out.println("===== STEP 6: GET USER DETAILS =====");
        validateAccountBE();
    }

    public void createAccount() {
        requestBody = new RequestUserObject("src/test/resources/createUser.json");
        accountService = new AccountService();

        ResponseUserObject responseBody = accountService.createAccount(requestBody);
        userId = responseBody.getUserID();
    }

    public void generateToken() {
        ResponseTokenObject responseBody = accountService.generateToken(requestBody);
        token = responseBody.getToken();
    }

    public void validateAccountBE() {
        accountService.validateAccountBE(token, userId);
    }

    public void loginApplication() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        driver = new ChromeDriver(options);
        driver.get("https://demoqa.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginIntoApplication(requestBody);
        loginPage.validateLoginError();
    }

    public void deleteAccountBE() {
        accountService.deleteAccountBE(token, userId);
    }
}


