package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import spec.Specification;

import static io.restassured.RestAssured.given;

public class UserAPI {
    private static final String REGISTER_PATH = "api/auth/register/";
    private static final String LOGIN_PATH = "api/auth/login/";
    private static final String LOGOUT_PATH = "api/auth/logout/";
    private static final String USER_PATH = "api/auth/user/";

    @Step("Создание нового пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(Specification.requestSpecification())
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserData credentials, String accessToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("authorization", accessToken)
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Разлогинивание")
    public ValidatableResponse logoutUser(String refreshToken) {
        return given()
                .spec(Specification.requestSpecification())
                .body(refreshToken)
                .when()
                .post(LOGOUT_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("authorization", accessToken)
                .when()
                .delete(USER_PATH)
                .then();
    }

    @Step("Получение пользовательских данных")
    public ValidatableResponse getUserData(String accessToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("authorization", accessToken)
                .when()
                .get(USER_PATH)
                .then();
    }
    @Step("Обновление данных пользователя")
    public ValidatableResponse updateUserData(User updatedUser, String accessToken) {
        return given()
                .spec(Specification.requestSpecification())
                .header("authorization", accessToken)
                .body(updatedUser)
                .when()
                .patch(USER_PATH)
                .then();
    }
}
