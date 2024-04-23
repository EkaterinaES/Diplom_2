import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class FailedLoginUserTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private StellarBurgersUser userMetods;
    private AuthUser authUser;
    private final String failedEmail = (new Random().nextInt(300) + "user@yandex.ru");
    private final String failedPassword = (new Random().nextInt(300) + "654765");


    @Before
    public void setUp() {
        authUser = new AuthUser(failedEmail, failedPassword);
        userMetods = new StellarBurgersUserImpl(StellarBurgersUserImpl.requestSpecification);
    }

    @Test
    @DisplayName("Login with failed password and email")
    @Description("Send POST request to /api/auth/login")
    public void loginUnderAnExistingUser() {
        Response responseAuth = userMetods.authUser(authUser);
        responseAuth.then().spec(responseSpecification)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}

