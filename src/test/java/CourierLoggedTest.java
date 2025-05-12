import config.Config;
import io.qameta.allure.*;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import org.junit.Test;// импортируем Test
import static steps.CourierSteps.*;  // или import steps.CounterSteps;
import io.qameta.allure.junit4.DisplayName; // импорт DisplayName
import org.junit.FixMethodOrder; //упорядочивние тестов в аллюр
import org.junit.runners.MethodSorters;


@Epic("API Курьеров")
@Feature("Логин курьера")

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourierLoggedTest {

    @Test
    @DisplayName("#1 - Успешная авторизация курьера с полными данными")
    @Description("Проверка, что API возвращает статус 200, в теле ответа id:")

    public void test1_creating_Courier_Logged() {

        String login = generateUniqueLogin(); // Генерация уникального логина

        Response createResponse = createCourier(login, Config.DEFAULT_PASSWORD, Config.DEFAULT_FIRST_NAME); // Отправляем запрос на создание курьера
        Response loggedResponse = courierLogged(login, Config.DEFAULT_PASSWORD); // Отправляем запрос на авторизацию

        int statusCode = loggedResponse.getStatusCode();                    // Получаем код ответа и тело ответа
        String responseBody = loggedResponse.getBody().asString();
        try {
            // Проверка тела ответа
            assertFalse("Тело ответа пустое", responseBody.isEmpty());

            // Проверяем наличие и непустое значение id
            assertTrue("Ответ не содержит поле 'id'", responseBody.contains("\"id\""));

            // Извлекаем id из JSON
            int id = loggedResponse.jsonPath().getInt("id");

            // Проверяем, что id не пустое (считаем что 0 недопустим)
            assertNotEquals(0, id); // Если 0 недопустим

            // проверяем код ответа на 200
            assertEquals("Неверный статус код при авторизации курьера",
                    Config.STATUS_CODE_OK,
                    statusCode);
        } finally {
            cleanUp(login, Config.DEFAULT_PASSWORD); // Удаляем созданного курьера, даже если упадет тест
        }
    }

    @Test
    @DisplayName("#2 - Авторизация курьера без логина → 400")
    @Description("Проверка, что API возвращает статус 400, Ответ: \"Недостаточно данных для входа\" ")
    public void test2_loggedCourierWithoutLogin_ShouldReturn400() {
        String json = "{"
                + "\"password\": \"" + Config.DEFAULT_PASSWORD + "\""
                + "}";
        Response response = courierLoggedPartial(json);
        assertEquals(
                "Ожидается статус 400 (Bad Request)",
                Config.STATUS_CODE_CLIENT_ERROR,
                response.getStatusCode());
        assertEquals(
                "Недостаточно данных для входа",
                response.jsonPath().getString("message")
        );
    }
    @Test
    @DisplayName("#3 - Авторизация курьера без пароля → 400")
    @Description("Проверка, что API возвращает статус 400, Ответ: \"Недостаточно данных для входа\" ")
    public void test3_loggedCourierWithoutPassword_ShouldReturn400() {
        String json = "{"
                + "\"login\": \"" + Config.DEFAULT_COURIER_LOGIN_PREFIX + "\""
                + "}";

            Response response = courierLoggedPartial(json);
            assertEquals(
                    "Ожидается статус 400 (Bad Reqest)",
                    Config.STATUS_CODE_CLIENT_ERROR,
                    response.getStatusCode());
            assertEquals(
                    "Недостаточно данных для входа",
                    response.jsonPath().getString("message")
            );

        }
    @Test
    @DisplayName("#4 - Авторизация курьера с password = \"\" → 400")
    @Description("Проверка, что API возвращает статус 400, Ответ: \"Недостаточно данных для входа\" ")
    public void test4_loggedCourierWithoutPasswordEmpty_Return400() {
        String json = "{"
                + "\"login\": \"" + Config.DEFAULT_COURIER_LOGIN_PREFIX + "\","
                + "\"password\": \"\""
                + "}";

        Response response = courierLoggedPartial(json);
        assertEquals(
                "Ожидается статус 400 (Bad Reqest)",
                Config.STATUS_CODE_CLIENT_ERROR,
                response.getStatusCode());
        assertEquals(
                "Недостаточно данных для входа",
                response.jsonPath().getString("message")
        );

    }

    @Test
    @DisplayName("#5 - Авторизация курьера с несуществующим логином")
    @Description("Проверка, что API возвращает статус 404, Ответ: \"Учетная запись не найдена\" ")
    public void test5_loggedCourierNonexistentLogin_ShouldReturn404() {

        String login = generateUniqueLogin(); // Генерация уникального логина
        Response loggedResponse = courierLogged(login, Config.DEFAULT_PASSWORD); // Отправляем запрос на авторизацию
        try {
            // Проверка тела ответа
            assertFalse("Тело ответа пустое", loggedResponse.getBody().asString().isEmpty());

            // Проверяем текст ответа
            assertEquals("Учетная запись не найдена", loggedResponse.jsonPath().getString("message"));
            // Ожидаем статус кода 404 (Not Found)
            assertEquals("Статус код должен быть 404 (Not Found)",
                    Config.STATUS_CODE_NOT_FOUND,
                    loggedResponse.getStatusCode());
        } finally {
            // Очистка, если нужно
        }
    }
    @Test
    @DisplayName("#6 - Авторизация курьера с неверным паролем")
    @Description("Проверка, что API возвращает статус 404, Ответ: \"Учетная запись не найдена\" ")
    public void test6_loggedCourierNonexistentPassword_ShouldReturn404() {

        String login = generateUniqueLogin(); // Генерация уникального логина
        Response createResponse = createCourier(login, Config.DEFAULT_PASSWORD, Config.DEFAULT_FIRST_NAME);// Отправляем запрос на создание курьера
        Response loggedResponse = courierLogged(login, Config.DEFAULT_PASSWORD+"xxx"); // Отправляем запрос на авторизацию

        try {
            // Проверка тела ответа
            assertFalse("Тело ответа пустое", loggedResponse.getBody().asString().isEmpty());
            // Проверяем текст ответа
            assertEquals("Учетная запись не найдена", loggedResponse.jsonPath().getString("message"));
            // Ожидаем статус кода 404 (Not Found)
            assertEquals("Статус код должен быть 404 (Not Found)",
                    Config.STATUS_CODE_NOT_FOUND,
                    loggedResponse.getStatusCode());
        } finally {
            cleanUp(login, Config.DEFAULT_PASSWORD); // Удаляем созданного курьера, даже если упадет тест
        }
    }


}

