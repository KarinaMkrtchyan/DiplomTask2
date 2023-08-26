package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import spec.Specification;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String ORDER_PATH = "api/orders";
    private static final String INGREDIENTS_PATH = "api/ingredients";

    @Step("создание заказа")
    public ValidatableResponse createOrder(List<String> ingredientIds, String accessToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("authorization", accessToken)
                .body(new Order(ingredientIds == null ? List.of() : ingredientIds))
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("получение заказа")
    public ValidatableResponse getOrders(String accessToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получение всех ингредиентов")
    public ValidatableResponse getListIngredients(String accessToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(INGREDIENTS_PATH)
                .then();

    }
}
