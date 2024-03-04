package tests;


import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.http.ContentType;


import tests.fileReader.ConfigFileReader;
import tests.helper.RequestBodyBuilder;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestMain {
    private static final Logger logger = Logger.getLogger(TestMain.class.getName());

    @BeforeTest
    void setup() {
        // Instantiate ConfigFileReader to get properties
        ConfigFileReader configFileReader = new ConfigFileReader();

        // Retrieve the base URL and bearer token from the config file
        String baseUrl = configFileReader.getApplicationUrl();
        String bearerToken = configFileReader.getToken();

        baseURI = baseUrl;
        RestAssured.baseURI = baseURI;

        // Print base URL for verification
        logger.info("Base URL: " + baseUrl);
        // Print bearer token for verification
        logger.info("Bearer Token: " + bearerToken);
    }

    private boolean userCreated = false;
    @Test(priority = 0, dataProvider = "userData")
    @Description("Creating a new user")
    void createUserTest(String id, String name, String email, String gender, String status) {
        if (!userCreated) {
            // Define the endpoint for creating a user
            String endpoint = EndPoints.CREATE_USER;

            // Generate the request body using the provided data
            String requestBody = RequestBodyBuilder.createUserRequestBody(id, name, email, gender, status);

            // Perform a POST request to create a user
            Response response = RestAssured
                    .given()
                    .header("Authorization", "Bearer 143b19e8fba61bef81a626502f2abc2a74533921876a3d4f215076fe2b5d9b9a") // Set the authorization header with the bearer token
                    .contentType(ContentType.JSON) // Set content type as JSON
                    .body(requestBody) // Set the request body
                    .post(endpoint);

            // Assert the response status code to ensure successful creation
            int statusCode = response.getStatusCode();
            Assert.assertEquals(statusCode, 201);
            // Log the response details
            logger.info("Response Body: " + response.getBody().asString());
            logger.info("Response Status Code: " + statusCode);
            logger.info("Response Time: " + response.getTime());
            logger.info("Response Status Line: " + response.getStatusLine());
            logger.info("Response Content Type: " + response.getHeader("content-type"));

            userCreated = true;
        } else {
            // Log that user creation is skipped since it's already been done
            logger.info("Skipping user creation as user is already created.");
        }
    }

    @DataProvider(name = "userData")
    public Object[][] userData() {
        return RequestBodyBuilder.userData();
    }

    @Test(priority = 1)
    @Description("Retrieve the data from the website")
    void getUserData() {
        //end - poitns
        String endpoint = EndPoints.USERS;
        // Perform a GET request to the specified endpoint
        Response response = given()
                .header("Authorization", "Bearer") // Set the authorization header with the bearer token
                .get(endpoint);

        //print the all data
        logger.info("Base URI: " + baseURI);
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Print each user's details
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        List<Map<String, Object>> users = jsonPath.getList("$");
        for (Map<String, Object> user : users) {
            logger.info("User ID: " + user.get("id"));
            logger.info("Name: " + user.get("name"));
            logger.info("Email: " + user.get("email"));
            logger.info("Gender: " + user.get("gender"));
            logger.info("Status: " + user.get("status"));
        }

        // Print the response body
        logger.info("Response Body: " + responseBody);

        // Assert that the response status code is 200 (OK)
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    @Description("Update user data on the website")
    void updateUser() {
        // Define the endpoint for updating user data
        String endpoint = EndPoints.UPDATE_USER;

        // Define the updated data
        String updatedUserData = "{ \"name\": \"vikas mishra\", \"email\": \"vikasone9@example.com\", \"gender\": \"male\", \"status\": \"active\" }";

        // Perform a PUT request to update the user data
        Response response = given()
                .header("Authorization", "Bearer 143b19e8fba61bef81a626502f2abc2a74533921876a3d4f215076fe2b5d9b9a") // Set the authorization header with the bearer token
                .contentType(ContentType.JSON) // Set content type as JSON
                .body(updatedUserData) // Set the updated data in the request body
                .when()
                .put(endpoint); // Specify the endpoint for updating user data

        // Log response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Print the response body
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    @Description("Patch user data on the website")
    void patchUser() {
        // Define the endpoint for patching user data
        String endpoint = EndPoints.PATCH_USER;

        // Define the updated data
        String patchedUserData = "{ \"name\": \"atulya\" }";

        // Perform a PATCH request to update the user data
        Response response = given()
                .header("Authorization", "Bearer 143b19e8fba61bef81a626502f2abc2a74533921876a3d4f215076fe2b5d9b9a")
                .contentType(ContentType.JSON)
                .body(patchedUserData)
                .when()
                .patch(endpoint);

        // Log response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Print the response body
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    @Description("Delete user data from the website")
    void deleteUser() {
        String endpoint = EndPoints.DELETE_USER;
        Response response = given()
                .header("Authorization", "Bearer 143b19e8fba61bef81a626502f2abc2a74533921876a3d4f215076fe2b5d9b9a")
                .when()
                .delete(endpoint);

        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Printing the response body here
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        Assert.assertEquals(response.getStatusCode(), 201);
    }
    @Test(priority = 5)
    @Description("Negative test case: Attempting to create a user with invalid data")
    void createInvalidUser() {
        // Defining the end points
        String endpoint = EndPoints.CREATE_USER;

        String invalidRequestBody = "{ \"name\": \"bdjsbj\" }"; // Missing required fields: id, email, gender, status

        // Perform a POST request to create a user with invalid data
        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer 143b19e8fba61bef81a626502f2abc2a74533921876a3d4f215076fe2b5d9b9a") // Set the authorization header with the bearer token
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .post(endpoint);

        // Assert the response status code to ensure the request failed
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(statusCode, 422);
        // Log the response details
        logger.info("Response Body: " + response.getBody().asString());
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }

    @Test(priority = 6)
    @Description("Negative test case: Attempting to delete a non-existent user")
    void deleteNonExistentUser() {
        // Define the endpoint for deleting a user
        String endpoint = EndPoints.DELETE_NON_EXISTENT_END_POINT;

        // Perform a DELETE request to delete a non-existent END POINT
        Response response = given()
                .header("Authorization", "Bearer 143b19e8fba61bef81a626502f2abc2a74533921876a3d4f215076fe2b5d9b9a")
                .when()
                .delete(endpoint);

        // Assert the response status code to ensure the request failed
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);

        // Log the response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }

    @Test(priority = 7)
    @Description("Negative test case: Attempting to perform operation on Invalid End Points")
    void invalidEndPoints() {
        // Define the endpoint for deleting a user
        String endpoint = EndPoints.INVALID_ENDPOINT;

        // Perform a DELETE request to delete a non-existent END POINT
        Response response = given()
                .header("Authorization", "Bearer 143b19e8fba61bef81a626502f2abc2a74533921876a3d4f215076fe2b5d9b9a")
                .when()
                .put(endpoint);

        //Asserting the response status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);

        // Log the response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }

    @Test(priority = 8)
    @Description("Negative test case: Attempting to perform operation on Invalid Token")
    void invalidToken() {
        // Define the endpoint for deleting a user
        String endpoint = EndPoints.PATCH_USER;

        // Perform a DELETE request to delete a non-existent END POINT
        Response response = given()
                .header("Authorization", "Bearer SDAGHDSDUHUHAW") //Invalid end point here
                .when()
                .put(endpoint);

        //Asserting the response status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);

        // Log the response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }


}





