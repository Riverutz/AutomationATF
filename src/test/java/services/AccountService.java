package services;

import client.APIClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import requestObject.RequestUserObject;
import responseObject.ResponseGetUserFailed;
import responseObject.ResponseGetUserObject;
import responseObject.ResponseTokenObject;
import responseObject.ResponseUserObject;

public class AccountService {

    public ResponseUserObject createAccount(RequestUserObject requestBody) {

        RequestSpecification request = RestAssured.given();
        request.body(requestBody);

        Response response = performRequest("POST", request, "/Account/v1/User/");
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);

        ResponseUserObject responseBody = response.getBody().as(ResponseUserObject.class);
        Assert.assertTrue(responseBody.getUsername().equals(requestBody.getUserName()));
        System.out.println(responseBody);
        return responseBody;
    }

    public ResponseTokenObject generateToken(RequestUserObject requestBody) {

        RequestSpecification request = RestAssured.given();
        request.body(requestBody);

        Response response = performRequest("POST", request, "/Account/v1/GenerateToken");
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getStatusLine().contains("OK"));

        ResponseTokenObject responseBody = response.getBody().as(ResponseTokenObject.class);
        System.out.println(responseBody.getToken());
        System.out.println(responseBody);

        return responseBody;
    }

    public void validateAccountBE(String token, String userId) {
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token);

        Response response = performRequest("GET", request, "/Account/v1/User/" + userId);

        if (response.getStatusCode() == 200) {
            ResponseGetUserObject responseBody = response.getBody().as(ResponseGetUserObject.class);
            System.out.println(responseBody.getUserId());
            System.out.println(responseBody);
        } else {
            ResponseGetUserFailed responseBody = response.getBody().as(ResponseGetUserFailed.class);
            System.out.println(responseBody.getMessage());
        }
    }
    public void deleteAccountBE(String token, String userId){
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token);

        Response response = performRequest("DELETE", request, "/Account/v1/User/" + userId);
        response.body().prettyPrint();
    }

    private Response performRequest(String requestType,
                                    RequestSpecification requestSpecification, String endpoint) {
        return new APIClient().performRequest(requestType, requestSpecification, endpoint);
    }
}
