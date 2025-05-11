import config.Config;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.Test;
import static steps.CourierSteps.*;
import io.qameta.allure.junit4.DisplayName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



@Epic("API Курьеров")
@Feature("Создание курьера")

public class CreateCourierTest {

    @Test
    @DisplayName("#1 Успешное создание с валидными данными")
    @Description("Проверка, что API возвращает статус 201, в теле ответа ok = true")

    public void Test1_creating_Courier_New() {

        String login = generateUniqueLogin(); // Генерация уникального логина для каждого теста

        Response createResponse = createCourier(login, Config.DEFAULT_PASSWORD, Config.DEFAULT_FIRST_NAME); // Отправляем запрос на создание курьера
        int statusCode = createResponse.getStatusCode();                    // Получаем код ответа и тело ответа
        String responseBody = createResponse.getBody().asString();
        try {
            boolean isOk = createResponse.jsonPath().getBoolean("ok");//извлекаем значение ok из ответа
            assertTrue("Поле 'ok' должно быть true", isOk); // Проверяем тело ответа ok = true
            // проверяем код ответа на 201
            assertEquals("Неверный статус код при создании курьера",
                    Config.STATUS_CODE_CREATED,
                    statusCode);
        } finally {
            cleanUp(login, Config.DEFAULT_PASSWORD); // Удаляем созданного курьера, даже если упадет тест
        }
    }


    @Test
    @DisplayName("#2 Создание дубля курьера приводит к ошибке")
    @Description("Проверка ошибки при создании курьера с существующим логином")
    public void Test2_creatingDuplicateCourier() {
        String login = generateUniqueLogin(); //генерируем уникальный логин

        // Создаем первого курьера
        Response firstResponse = createCourier(login, Config.DEFAULT_PASSWORD, Config.DEFAULT_FIRST_NAME);
        assertEquals(Config.STATUS_CODE_CREATED, firstResponse.getStatusCode());

        try {
            // Пытаемся создать дубликат
            Response duplicateResponse = createCourier(login, "anotherPassword", "AnotherName");

            // Проверяем статус код
            assertEquals("Неверный статус код при дублировании курьера",
                    Config.STATUS_CODE_CONFLICT,
                    duplicateResponse.getStatusCode());

            // Проверяем тело ответа
            String actualMessage = duplicateResponse.jsonPath().getString("message");
            String expectedMessage = "Этот логин уже используется. Попробуйте другой.";
            assertEquals("Неверное сообщение об ошибке",
                    expectedMessage,
                    actualMessage);

            // Проверяем код ошибки
            int actualCode = duplicateResponse.jsonPath().getInt("code");
            assertEquals("Неверный код ошибки", Config.STATUS_CODE_CONFLICT, actualCode);

        } finally {
            cleanUp(login, Config.DEFAULT_PASSWORD); // Удаляем созданного курьера, даже если упадет тест
        }
    }

    @Test
    @DisplayName("#3 Создание курьера без логина → 400")
    @Description("Проверка ошибки при отсутствии логина")
    public void Test3_createCourierWithoutLogin_ShouldReturn400() {
        String json = "{"
                + "\"password\": \"" + Config.DEFAULT_PASSWORD + "\","
                + "\"firstName\": \"" + Config.DEFAULT_FIRST_NAME + "\""
                + "}";
        Response response = createCourierPartial(json);
        assertEquals(400, response.getStatusCode());
        assertEquals(
                "Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @DisplayName("#4 Создание курьера без пароля → 400")
    public void Test4_createCourierWithoutPassword_ShouldReturn400() {
        String json = "{"
                + "\"login\": \"" + Config.DEFAULT_COURIER_LOGIN_PREFIX + "\","
                + "\"firstName\": \"" + Config.DEFAULT_FIRST_NAME + "\""
                + "}";
        Response response = createCourierPartial(json);
        assertEquals(400, response.getStatusCode());
        assertEquals(
                "Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message")
        );

        assertEquals(400, response.getStatusCode());
    }
    @Test
    @DisplayName("#5 Создание курьера без имени → 400")
    public void Test5_createCourierWithoutFirstName_ShouldReturn400() {
        String json = "{"
                + "\"login\": \"" + Config.DEFAULT_COURIER_LOGIN_PREFIX + "\","
                + "\"password\": \"" + Config.DEFAULT_PASSWORD + "\""
                + "}";
        Response response = createCourierPartial(json);

        try {
            // Проверяем ошибку
            assertEquals(
                    "Недостаточно данных для создания учетной записи",
                    response.jsonPath().getString("message")
            );
            assertEquals(400, response.getStatusCode());
        } finally {
            // Пытаемся удалить на случай, если курьер создался
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201 || response.getStatusCode() == 409) {
                cleanUp(Config.DEFAULT_COURIER_LOGIN_PREFIX, Config.DEFAULT_PASSWORD);
            }


            assertEquals(400, response.getStatusCode());
        }
    }

}


