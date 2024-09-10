package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProfilePage;
import requestObject.RequestUserObject;
import responseObject.ResponseTokenObject;
import responseObject.ResponseUserObject;

import java.time.Duration;

public class CreateUserFETest {

    public String baseURI = "https://demoqa.com";
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
        //VERIFIC GET IN BACKEND folosind userID si token
        validateAccountBE();

        System.out.println("===== STEP 4: LOGIN USER =====");
        loginApplication();

        System.out.println("===== STEP 5: DELETE USER =====");
        deleteAccountFE();

        System.out.println("===== STEP 6: GET USER DETAILS =====");
        //VERIFIC GET IN BACKEND folosind userID si token
        validateAccountBE();
    }

    public void createAccount() {

        //DEFINESC CONFIGURAREA CLIENTULUI, APOI DEFINIM UN REQUEST

        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(baseURI);

        requestBody = new RequestUserObject("src/test/resources/createUser.json");

        //ADAUGAM REQUEST BODY
        System.out.println(requestBody);
        request.body(requestBody);

        //EXECUTAM REQUEST-ul DE TIP POST LA UN ENDPOINT SPECIFIC
        Response response = request.post("/Account/v1/User");

        //VALIDAM RESPONSE STATUS CODE
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);

        ResponseUserObject responseBody = response.getBody().as(ResponseUserObject.class);
        Assert.assertTrue(responseBody.getUsername().equals(requestBody.getUserName()));
        System.out.println(responseBody);
        userId = responseBody.getUserID();

        //VALIDAM STATUS
        Assert.assertTrue(response.getStatusLine().contains("Created"));
    }

    public void generateToken() {

        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(baseURI);

        request.body(requestBody);

        Response response = request.post("/Account/v1/GenerateToken");

        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getStatusLine().contains("OK"));

        ResponseTokenObject responseBody = response.getBody().as(ResponseTokenObject.class);
        System.out.println(responseBody.getToken());
        System.out.println(responseBody);
        token = responseBody.getToken();
    }

    public void validateAccountBE() {

        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(baseURI);
        request.header("Authorization", "Bearer " + token);

        Response response = request.get("/Account/v1/User/" + userId);
        response.body().prettyPrint();
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

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.validateLoginProcess(requestBody);
    }

    public void deleteAccountFE(){
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.deleteAccount();
    }
}

