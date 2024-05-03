import io.restassured.response.Response;

public interface StellarBurgersUser {
    Response createUser(User user);

    void deleteUser(String accessToken);

    Response authUser(AuthUser authUser);

    Response dataUpdate(User user, String accessToken);

    Response dataUpdateWithoutAuth(User user);

    void logoutUser(String refreshToken);

}
