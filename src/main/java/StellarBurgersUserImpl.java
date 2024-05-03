import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class StellarBurgersUserImpl implements StellarBurgersUser {
    public static RequestSpecification requestSpecification =
            new RequestSpecBuilder()
                    .log(LogDetail.ALL)
                    .addHeader("Content-type", "application/json")
                    .setBaseUri("https://stellarburgers.nomoreparties.site/")
                    .build();

    public StellarBurgersUserImpl(RequestSpecification requestSpecification) {
        StellarBurgersUserImpl.requestSpecification = requestSpecification;
    }

    private static final String CREATE_USER_ENDPOINT = "/api/auth/register";
    private static final String DELETE_USER_ENDPOINT = "/api/auth/user";
    private static final String AUTH_USER_ENDPOINT = "/api/auth/login";
    private static final String DATA_UPDATE_ENDPOINT = "/api/auth/user";
    private static final String LOGOUT_USER_ENDPOINT = "api/auth/logout";

    @Override
    @Step("Creating a user")
    public Response createUser(User user) {
        return given()
                .spec(requestSpecification)
                .body(user)
                .post(CREATE_USER_ENDPOINT);
    }

    @Override
    @Step("Deleting a user")
    public void deleteUser(String accessToken) {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .spec(requestSpecification)
                .delete(DELETE_USER_ENDPOINT);
    }


    @Override
    @Step("User authorization")
    public Response authUser(AuthUser authUser) {
        return given()
                .spec(requestSpecification)
                .body(authUser)
                .post(AUTH_USER_ENDPOINT);
    }

    @Override
    @Step("Changing user data")
    public Response dataUpdate(User user, String accessToken) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .spec(requestSpecification)
                .body(user)
                .patch(DATA_UPDATE_ENDPOINT);
    }

    @Override
    @Step("Changing user data without authorization")
    public Response dataUpdateWithoutAuth(User user) {
        return given()
                .spec(requestSpecification)
                .body(user)
                .patch(DATA_UPDATE_ENDPOINT);
    }

    @Override
    @Step("User logout")
    public void logoutUser(String refreshToken) {
        given()
                .spec(requestSpecification)
                .body(refreshToken)
                .patch(LOGOUT_USER_ENDPOINT);
    }
}
