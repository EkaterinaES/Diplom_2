import io.restassured.response.Response;

public interface StellarBurgersUser {
    Response createUser(User user);

    Response deleteUser(String accessToken);

    Response authUserToken(AuthUserWithToken authUserWithToken);

    Response authUser(AuthUser authUser);

}
