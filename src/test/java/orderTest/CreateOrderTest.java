package orderTest;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import order.OrderApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserAPI;
import ingredient.Ingridients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

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
    @Description("Создание нового заказа с ингредиентами и авторизацией")
    public void testCreateOrderWithIngredientsAndAuthorization() {
        List<String> ingredientIds = getListIngridient(3);
        ValidatableResponse response = orderApi.createOrder(ingredientIds, accessToken);
        response.statusCode(200);
        response.body("success", equalTo(true));
        response.body("order.number", notNullValue());
    }

    @Test
    @Description("Создание нового заказа без ингредиентов и авторизации")
    public void createOrderWithoutAuthorization() {
        ValidatableResponse response = orderApi.createOrder(null, "");
        response.statusCode(400);
        response.body("success", equalTo(false));
        response.body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Создание с неверным хешем ингредиентов")
    public void testCreateOrderWithInvalidIngredientsAndAuthorization() {
        List<String> ingredientIds = Arrays.asList("invalid_hash1", "invalid_hash2");
        ValidatableResponse response = orderApi.createOrder(ingredientIds, accessToken);
        response.statusCode(500);
    }

    @Test
    @Description("Создание нового заказа с авторизацией, но без ингредиентов")
    public void testCreateOrderWithAuthorizationWithoutIngredients() {
        ValidatableResponse response = orderApi.createOrder(null, accessToken);
        response.statusCode(400);
        response.body("success", equalTo(false));
        response.body("message", equalTo("Ingredient ids must be provided"));
    }

    private List getListIngridient(int count) {
        ValidatableResponse response = orderApi.getListIngredients(accessToken);
        Ingridients listIngredients = response.extract().as(Ingridients.class);
        List<String> ingredientIds = new ArrayList<>(listIngredients.getData().size());
        for (int i = 0; i < (Math.min(listIngredients.getData().size(), count)); i++) {
            String id = listIngredients.getData().get(i).get_id();
            ingredientIds.add(id);
        }
        return ingredientIds;
    }

}