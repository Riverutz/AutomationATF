package client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIClient {
    public String baseURI = "https://demoqa.com";

    //CLASA IN CARE CONFIGURAM CLIENTUL
    //FACEM O METODA IN CARE SETAM CONFIGURARILE DE CLIENT
    //FACEM O METODA IN CARE SETAM ACTIUNEA PE CARE O FACE CLIENTUL

    public Response performRequest(String requestType, RequestSpecification requestSpecification, String endpoint) {
        switch (requestType) {
            case "POST":
                return prepareClient(requestSpecification).post(endpoint);
            case "GET":
                return prepareClient(requestSpecification).get(endpoint);
            case "DELETE":
                return prepareClient(requestSpecification).delete(endpoint);
        }
        return null;
    }

    public RequestSpecification prepareClient(RequestSpecification requestSpecification) {
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.baseUri(baseURI);
        return requestSpecification;
    }
}
