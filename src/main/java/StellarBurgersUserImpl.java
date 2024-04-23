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

    @Override
    public Response createUser(User user) {
        return given()
                .spec(requestSpecification)
                .body(user)
                .post(CREATE_USER_ENDPOINT);
    }

    @Override
    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .spec(requestSpecification)
                .delete(DELETE_USER_ENDPOINT);
    }


    @Override
    public Response authUser(AuthUser authUser) {
        return given()
                .spec(requestSpecification)
                .body(authUser)
                .post(AUTH_USER_ENDPOINT);
    }

    @Override
    public Response authUserToken(AuthUserWithToken authUserWithToken) {
        return given()
                .spec(requestSpecification)
                .body(authUserWithToken)
                .post(AUTH_USER_ENDPOINT);
    }

}
