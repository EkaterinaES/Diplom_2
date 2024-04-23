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

public class CreateUserTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private User user;
    private StellarBurgersUser userMetods;
    private final String email = (new Random().nextInt(300) + "user@yandex.ru");
    private final String password = (new Random().nextInt(300) + "654765");
    private final String name = ("Ivan" + new Random().nextInt(300));
    private String accessToken;


    @Before
    public void setUp() {
        user = new User(email, password, name);
        userMetods = new StellarBurgersUserImpl(StellarBurgersUserImpl.requestSpecification);
    }

    @Test
    @DisplayName("Сreating a user without unique email, password and name")
    @Description("Send POST request to /api/auth/register")
    public void createUniqueUser() {
        Response response = userMetods.createUser(user);
        this.accessToken = response.then().extract().body().path("accessToken");
        response.then()
                .spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
//        authUser = new AuthUser(email, password, accessToken);
//        Response response1 = userMetods.authUser(authUser);//авторизоваться
//        response1.then().log().all().assertThat().body("success", equalTo(true))
//                .and()
//                .statusCode(200);

    }

    @Test
    @DisplayName("Сreating a user who has already been registered")
    @Description("Send POST request to /api/auth/register")
    public void createCreatingAnExistingUser() {
        Response response = userMetods.createUser(user);
        this.accessToken = response.then().extract().body().path("accessToken");
        Response responseRepeated = userMetods.createUser(user);
        responseRepeated.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @After
    public void cleanUp() {
        StringBuilder accessTokenWithoutBearer = new StringBuilder(accessToken);
        accessTokenWithoutBearer.delete(0, 7);
        String accessTokenForDelete = accessTokenWithoutBearer.toString();
        Response response = userMetods.deleteUser(accessTokenForDelete);
        response.then()
                .spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(202);
    }
}
