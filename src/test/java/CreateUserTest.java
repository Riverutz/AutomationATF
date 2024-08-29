import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateUserTest {

    public String baseURI = "https://demoqa.com";
    public JSONObject requestBody;

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

        String username = "DanielTesting" + System.currentTimeMillis();
        requestBody = new JSONObject();
        requestBody.put("userName", username);
        requestBody.put("password", "Automation24#'#!");

        //ADAUGAM REQUEST BODY
        request.body(requestBody.toString());

        //EXECUTAM REQUEST-ul DE TIP POST LA UN ENDPOINT SPECIFIC
        Response response = request.post("/Account/v1/User");

        //VALIDAM RESPONSE STATUS CODE
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);

        ResponseBody responseBody = response.getBody();
        Assert.assertTrue(responseBody.asPrettyString().contains(username));

        //VALIDAM STATUS
        Assert.assertTrue(response.getStatusLine().contains("Created"));

    }

    public void generateToken() {

        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(baseURI);

        request.body(requestBody.toString());

        Response response = request.post("/Account/v1/GenerateToken");

        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);

        ResponseBody responseBody = response.getBody();
        Assert.assertTrue(responseBody.asPrettyString().contains("token"));
        Assert.assertTrue(response.getStatusLine().contains("OK"));
    }
}

