import io.restassured.response.Response;

public interface StellarBurgersOrder {
    Response creatOrderByAuthorizedUser(Order order, String accessToken);
    Response creatOrderWithoutAuth(Order order);
    Response getOrdersNotAuthUser();
    Response getOrdersAuthUser(String accessToken);

}
