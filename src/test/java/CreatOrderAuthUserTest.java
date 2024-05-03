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

public class CreatOrderAuthUserTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private StellarBurgersUser userMetods;
    private StellarBurgersOrder orderMetods;
    private Order order;
    private Order orderWithoutIngredients;
    private Order ordersWithInvalidHash;
    private String accessTokenForAuthAndDelete;
    private final String email = (new Random().nextInt(300) + "user@yandex.ru");
    private final String password = (new Random().nextInt(300) + "654765");
    private final String name = ("Ivan" + new Random().nextInt(300));
    String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"};
    String[] withoutIngredients = {};
    String[] ingredientsWithInvalidHash = {"c0c5a71d1f82001bdaaa6d", "11c0c5a71d1f82001bdaaa6d",};


    @Before
    public void setUp() {
        User user = new User(email, password, name);
        userMetods = new StellarBurgersUserImpl(StellarBurgersUserImpl.requestSpecification);
        orderMetods = new StellarBurgersOrderImpl(StellarBurgersOrderImpl.requestSpecification);
        order = new Order(ingredients);//создали объект с ингредиентами
        orderWithoutIngredients = new Order(withoutIngredients);//создали объект без ингредиентов
        ordersWithInvalidHash = new Order(ingredientsWithInvalidHash);
        Response responseCreat = userMetods.createUser(user);//создали пользователя
        String accessToken = responseCreat.then().extract().body().path("accessToken");//получили токен
        StringBuilder accessTokenWithoutBearer = new StringBuilder(accessToken);
        accessTokenForAuthAndDelete = accessTokenWithoutBearer.delete(0, 7).toString();//преобразовали токен для дальнейших запросов
    }

    @Test
    @DisplayName("Сreating an order with ingredients by an authorized user.")
    @Description("Send POST request to /api/orders")
    public void createOrderWithIngredientsAfterAuth() {
        Response response = orderMetods.creatOrderByAuthorizedUser(order, accessTokenForAuthAndDelete);
        response.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("order.owner.email", equalTo(email))
                .statusCode(200);
    }
    @Test
    @DisplayName("Сreating an order without ingredients by an authorized user.")
    @Description("Send POST request to /api/orders")
    public void createOrderWithoutIngregientsAfterAuth() {
        Response response = orderMetods.creatOrderByAuthorizedUser(orderWithoutIngredients, accessTokenForAuthAndDelete);
        response.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(false), "message", equalTo("Ingredient ids must be provided"))
                .statusCode(400);
    }
    @Test
    @DisplayName("Сreating an order with invalid hash by an authorized user.")
    @Description("Send POST request to /api/orders")
    public void createOrderWithInvalidHashAfterAuth() {
        Response response = orderMetods.creatOrderByAuthorizedUser(ordersWithInvalidHash, accessTokenForAuthAndDelete);
        response.then().spec(responseSpecification)
                .statusCode(500);
    }
        @After
        public void cleanUp () {
            userMetods.deleteUser(accessTokenForAuthAndDelete);
        }

}

