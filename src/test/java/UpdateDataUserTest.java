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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateDataUserTest {
    public static ResponseSpecification responseSpecification =
            new ResponseSpecBuilder()
                    .log(LogDetail.ALL)
                    .build();
    private StellarBurgersUser userMetods;
    private User userWithUpdateData;

    private final String email = (new Random().nextInt(300) + "user@yandex.ru");
    private final String password = (new Random().nextInt(300) + "654765");
    private final String name = ("Ivan" + new Random().nextInt(300));
    private String accessTokenForUpdateAndDelete;
    private String refreshToken;
    private final String updateEmail = (new Random().nextInt(300) + "ur@yandex.ru");
    private final String updatePassword = (new Random().nextInt(300) + "999999");
    private final String updateName = ("Vania" + new Random().nextInt(300));

    @Before
    public void setUp() {
        User user = new User(email, password, name);
        userMetods = new StellarBurgersUserImpl(StellarBurgersUserImpl.requestSpecification);
        Response responseCreat = userMetods.createUser(user);//создали пользователя
        String accessToken = responseCreat.then().extract().body().path("accessToken");//получили токен
        refreshToken = responseCreat.then().extract().body().path("refreshToken");
        StringBuilder accessTokenWithoutBearer = new StringBuilder(accessToken);
        accessTokenForUpdateAndDelete = accessTokenWithoutBearer.delete(0, 7).toString();//преобразовали токен для дальнейших запросов
        userWithUpdateData = new User(updateEmail, updatePassword, updateName);//создали объект с новыми данными

    }

    @DisplayName("Change user data with authorized")
    @Description("Send PATCH request to /api/auth/user")
    @Test
    public void UpdateDataWithAuth() {
        Response responseUpdate = userMetods.dataUpdate(userWithUpdateData, accessTokenForUpdateAndDelete);
        responseUpdate.then()
                .spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", containsString(updateEmail))
                .assertThat().body("user.name", containsString(updateName))
                .and().statusCode(200);
        userMetods.logoutUser(refreshToken);//выйти из системы, чтобы зайти с новым паролем и проверить поменялся ли он
        AuthUser loginWithUpdateData = new AuthUser(updateEmail, updatePassword);
        Response responseLoginWithUpdateData = userMetods.authUser(loginWithUpdateData);//залогиниться с новыми email и password
        responseLoginWithUpdateData.then()
                .spec(responseSpecification)
                .assertThat().body("success", equalTo(true))
                .and().statusCode(200);
    }

    @DisplayName("Do not update user data without authorized")
    @Description("Send PATCH request to /api/auth/user")
    @Test
    public void DoNotUpdateDataWithoutAuth() {
        Response responseUpdate = userMetods.dataUpdateWithoutAuth(userWithUpdateData);
        responseUpdate.then()
                .spec(responseSpecification)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(401);
    }
    @After
    public void cleanUp() {
        userMetods.deleteUser(accessTokenForUpdateAndDelete);
    }
}
