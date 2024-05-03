import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderAuthUserTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private StellarBurgersUser userMetods;
    private StellarBurgersOrder orderMetods;
    private String accessTokenForAuthAndDelete;
    private final String email = (new Random().nextInt(300) + "user@yandex.ru");
    private final String password = (new Random().nextInt(300) + "654765");
    private final String name = ("Ivan" + new Random().nextInt(300));
    String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"};


    @Before
    public void setUp() {
        User user = new User(email, password, name);
        userMetods = new StellarBurgersUserImpl(StellarBurgersUserImpl.requestSpecification);
        orderMetods = new StellarBurgersOrderImpl(StellarBurgersOrderImpl.requestSpecification);
        Order order = new Order(ingredients);//создали объект с ингредиентами
        Response responseCreat = userMetods.createUser(user);//создали пользователя
        String accessToken = responseCreat.then().extract().body().path("accessToken");//получили токен
        StringBuilder accessTokenWithoutBearer = new StringBuilder(accessToken);
        accessTokenForAuthAndDelete = accessTokenWithoutBearer.delete(0, 7).toString();//преобразовали токен для дальнейших запросов
        orderMetods.creatOrderByAuthorizedUser(order, accessTokenForAuthAndDelete);//создали заказ
    }

    @Test
    @DisplayName("Receive orders from an authorized user")
    @Description("Send GET request to /api/orders")
    public void getOrdersAuthUser() {
        Response response = orderMetods.getOrdersAuthUser(accessTokenForAuthAndDelete);
        response.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .statusCode(200);
    }
    @After
    public void cleanUp () {
        userMetods.deleteUser(accessTokenForAuthAndDelete);
    }

}

