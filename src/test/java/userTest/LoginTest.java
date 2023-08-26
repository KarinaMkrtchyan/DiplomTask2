package userTest;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserAPI;
import user.UserData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;

public class LoginTest {

    private UserAPI UserAPI;
    private User testUser;

    @Before
    public void setUp() {
        UserAPI = new UserAPI();
        // Регистрация нового пользователя для тестов
        testUser = User.getDefaultUser();
        UserAPI.createUser(testUser);
    }

    @After
    public void tearDown() {
        // Удаление тестового пользователя после выполнения тестов
        if (testUser != null) {
            ValidatableResponse response = UserAPI.loginUser(testUser, "");
            String accessToken = response.extract().path("accessToken");
            UserAPI.deleteUser(accessToken).statusCode(202);
        }
    }

    @Test
    @Description("Тест успешной авторизации пользователя")
    public void testSuccessfulUserLogin() {
        // Логин с правильными данными
        ValidatableResponse response = UserAPI.loginUser(testUser, "");
        response.statusCode(200);
        response.body("success", equalTo(true));
        assertNotNull("Access token should not be null", response.extract().path("accessToken"));
    }

    @Test
    @Description("Тест логина с некорректными данными")
    public void testUserLoginWithInvalidCredentials() {
        // Создание объекта с некорректными данными
        UserData invalidCredentials = new UserData("invalid_email@example.com", "invalid_password");

        // Логин с неверными данными
        ValidatableResponse response = UserAPI.loginUser(invalidCredentials, "");
        response.statusCode(401);
        response.body("success", equalTo(false));
        response.body("message", equalTo("email or password are incorrect"));
    }

}