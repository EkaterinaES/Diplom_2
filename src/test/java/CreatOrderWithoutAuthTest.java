import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.equalTo;

public class CreatOrderWithoutAuthTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private StellarBurgersOrder orderMetods;
    private Order order;
    Order orderWithoutIngredients;
    String[] ingredients = {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"};
    String[] withoutIngredients = {};


    @Before
    public void setUp() {
        orderMetods = new StellarBurgersOrderImpl(StellarBurgersOrderImpl.requestSpecification);
        order = new Order(ingredients);//создали объект с ингредиентами
        orderWithoutIngredients = new Order(withoutIngredients);//создали объект без ингредиентов
    }

    @Test
    @DisplayName("Сreating an order with ingredients by unauthorized user.")
    @Description("Send POST request to /api/orders")
    public void creatOrderWithoutAuth() {
        Response response = orderMetods.creatOrderWithoutAuth(order);
        response.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .statusCode(200);
    }
    @Test
    @DisplayName("Сreating an order without ingredients by unauthorized user.")
    @Description("Send POST request to /api/orders")
    public void creatOrderWithoutIngredientAndWithoutAuth() {
        Response response = orderMetods.creatOrderWithoutAuth(orderWithoutIngredients);
        response.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(false), "message", equalTo("Ingredient ids must be provided"))
                .statusCode(400);

    }
}
