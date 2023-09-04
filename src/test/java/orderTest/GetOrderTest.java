package orderTest;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import order.OrderApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserAPI;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasEntry;

public class GetOrderTest {
    private UserAPI UserAPI;
    private OrderApi orderApi;
    private User testUser;
    private String accessToken;

    @Before
    public void setUp() {
        UserAPI = new UserAPI();
        orderApi = new OrderApi();
        testUser = User.getDefaultUser();
        UserAPI.createUser(testUser);
        ValidatableResponse loginResponse = UserAPI.loginUser(testUser, "");
        accessToken = loginResponse.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (testUser != null) {
            UserAPI.deleteUser(accessToken).statusCode(202);
        }
    }

    @Test
    @Description("Получение заказов авторизованным пользователем")
    public void testGetOrdersByAuthorizedUser() {

        // Создание заказа для авторизованного пользователя
        ValidatableResponse createOrderResponse = orderApi.createOrder(List.of("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70"), accessToken);
        createOrderResponse.statusCode(200);
        createOrderResponse.body("success", equalTo(true));

        // Получение заказов
        ValidatableResponse response = orderApi.getOrders(accessToken);
        response.statusCode(200);
        response.body("success", equalTo(true));

        // Проверка, что в полученных заказах есть созданный заказ
        response.body("orders", hasItem(hasEntry("number", createOrderResponse.extract().path("order.number"))));
    }

    @Test
    @Description("Получение заказов неавторизованным пользователем")
    public void testGetOrdersByUnauthorizedUser() {
        ValidatableResponse response = orderApi.getOrders("");
        response.statusCode(401);
        response.body("success", equalTo(false));
        response.body("message", equalTo("You should be authorised"));
    }
}