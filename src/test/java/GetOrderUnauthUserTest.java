import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderUnauthUserTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private StellarBurgersOrder orderMetods;

    @Before
    public void setUp() {
        orderMetods = new StellarBurgersOrderImpl(StellarBurgersOrderImpl.requestSpecification);
    }

    @Test
    @DisplayName("Receive orders from an unauthorized user")
    @Description("Send GET request to /api/orders")
    public void getOrdersUnauthUser() {
        Response response = orderMetods.getOrdersNotAuthUser();
        response.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(false), "message", equalTo("You should be authorised"))
                .statusCode(401);
    }

}

