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

public class LoginUserTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private User user;
    private StellarBurgersUser userMetods;
    private AuthUser authUser;
    private final String email = (new Random().nextInt(300) + "user@yandex.ru");
    private final String password = (new Random().nextInt(300) + "654765");
    private final String name = ("Ivan" + new Random().nextInt(300));
    private String accessToken;


    @Before
    public void setUp() {
        user = new User(email, password, name);
        authUser = new AuthUser(email, password);
        userMetods = new StellarBurgersUserImpl(StellarBurgersUserImpl.requestSpecification);
    }

    @Test
    @DisplayName("Login under an existing user")
    @Description("Send POST request to /api/auth/login")
    public void loginUnderAnExistingUser() {
        Response responseCreat = userMetods.createUser(user);
        this.accessToken = responseCreat.then().extract().body().path("accessToken");
        Response responseAuth = userMetods.authUser(authUser);
        responseAuth.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @After
    public void cleanUp() {
        StringBuilder accessTokenWithoutBearer = new StringBuilder(accessToken);
        accessTokenWithoutBearer.delete(0, 7);
        String accessTokenForDelete = accessTokenWithoutBearer.toString();
        userMetods.deleteUser(accessTokenForDelete);
    }
}
