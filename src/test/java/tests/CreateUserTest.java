package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import requestObject.RequestUserObject;
import responseObject.ResponseTokenObject;
import responseObject.ResponseUserObject;

public class CreateUserTest {

    public String baseURI = "https://demoqa.com";
    public RequestUserObject requestBody;

    @Test
    public void testMethod() {
        System.out.println("===== STEP 1: Create Account =====");
        createAccount();

        System.out.println("===== STEP 2: Generate Token =====");
        generateToken();
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

        ResponseTokenObject responseBody = response.getBody().as(ResponseTokenObject.class);
        System.out.println(responseBody.getToken());
        Assert.assertTrue(response.getStatusLine().contains("OK"));
    }
}

