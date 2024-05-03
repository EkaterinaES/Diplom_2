import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class StellarBurgersOrderImpl implements StellarBurgersOrder {
    public static RequestSpecification requestSpecification =
            new RequestSpecBuilder()
                    .log(LogDetail.ALL)
                    .addHeader("Content-type", "application/json")
                    .setBaseUri("https://stellarburgers.nomoreparties.site/")
                    .build();

    public StellarBurgersOrderImpl(RequestSpecification requestSpecification) {
        StellarBurgersOrderImpl.requestSpecification = requestSpecification;
    }

    private static final String CREAT_ORDER_ENDPOINT = "/api/orders";
    private static final String GET_ORDER_ENDPOINT = "/api/orders";


    @Override
    @Step("Creating an order by an authorized user")
    public Response creatOrderByAuthorizedUser(Order order, String accessToken) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .spec(requestSpecification)
                .body(order)
                .post(CREAT_ORDER_ENDPOINT);
    }

    @Override
    @Step("Creating an order without user authorization")
    public Response creatOrderWithoutAuth(Order order) {
        return given()
                .spec(requestSpecification)
                .body(order)
                .post(CREAT_ORDER_ENDPOINT);
    }

    @Override
    @Step("Receiving an order from an unauthorized user")
    public Response getOrdersNotAuthUser() {
        return given()
                .spec(requestSpecification)
                .get(GET_ORDER_ENDPOINT);
    }

    @Override
    @Step("Receiving an order from an authorized use")
    public Response getOrdersAuthUser(String accessToken) {
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .spec(requestSpecification)
                .get(GET_ORDER_ENDPOINT);
    }
}
