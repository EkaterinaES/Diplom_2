import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class FailedUserCreationTest {
    public static ResponseSpecification responseSpecification=
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private User user;
    private StellarBurgersUser userMetods;
    private final String email;
    private final String password;
    private final String name;

    public FailedUserCreationTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"", "12345", ("Ivan" + new Random().nextInt(300))},
                {(new Random().nextInt(300) + "user@yandex.ru"), "", ("Ivan" + new Random().nextInt(300))},
                {(new Random().nextInt(300) + "user@yandex.ru"), "12345", ""},

        };
    }

    @Before
    public void setUp() {
        user = new User(email, password, name);
        userMetods = new StellarBurgersUserImpl(StellarBurgersUserImpl.requestSpecification);
    }

    @Test
    @DisplayName("Сreating a user without one of the required parameters")
    @Description("Send POST request to /api/auth/register")
    public void createUniqueUser() {
        Response response = userMetods.createUser(user);
        response.then()
                .spec(responseSpecification)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);

    }
}

