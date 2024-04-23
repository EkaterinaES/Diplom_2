import io.restassured.response.Response;

public interface StellarBurgersUser {
Response createUser(User user);
Response deleteUser(String accessToken);
Response authUser(AuthUser authUser);

}
