import config.Config;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.Assert;  // Для JUnit 4
import org.junit.Test;// импортируем Test
import static steps.CourierSteps.*;  // или import steps.CounterSteps;
import io.qameta.allure.junit4.DisplayName; // импорт DisplayName


@Epic("API Курьеров")
@Feature("Создание курьера")

public class CreateCourierTest {

    @Test
    @DisplayName("Создание тестового курьера")
    @Story("Успешное создание с валидными данными")
    @Description("Проверка, что API возвращает статус 201, в теле ответа ok = true")

    public void Creating_Courier_New() {

        String login = generateUniqueLogin(); // Генерация уникального логина для каждого теста

        Response createResponse = createCourier(login, Config.DEFAULT_PASSWORD, Config.DEFAULT_FIRST_NAME); // Отправляем запрос на создание курьера
        int statusCode = createResponse.getStatusCode();                    // Получаем код ответа и тело ответа
        String responseBody = createResponse.getBody().asString();
        try {
            // Проверяем тело ответа ok = true
            boolean isOk = createResponse.jsonPath().getBoolean("ok");//извлекаем значение ok из ответа
            Assert.assertTrue("Поле 'ok' должно быть true", isOk);
            // проверяем код ответа на 201
            Assert.assertEquals("Неверный статус код при создании курьера",
                    Config.STATUS_CODE_CREATED,
                    statusCode);
        } finally {
            cleanUp(login, Config.DEFAULT_PASSWORD); // Удаляем созданного курьера, даже если упадет тест
        }
    }


    @Test
    @DisplayName("Создание дубликата курьера")
    @Story("Создание дубля курьера приводит к ошибке")
    @Description("Проверка ошибки при создании курьера с существующим логином")
    public void creatingDuplicateCourier() {
        String login = generateUniqueLogin(); //генерируем уникальный логин

        // 1. Создаем первого курьера
        Response firstResponse = createCourier(login, Config.DEFAULT_PASSWORD, Config.DEFAULT_FIRST_NAME);
        Assert.assertEquals(Config.STATUS_CODE_CREATED, firstResponse.getStatusCode());

        try {
            // 2. Пытаемся создать дубликат
            Response duplicateResponse = createCourier(login, "anotherPassword", "AnotherName");

            // Проверяем статус код
            Assert.assertEquals("Неверный статус код при дублировании курьера",
                    Config.STATUS_CODE_CONFLICT,
                    duplicateResponse.getStatusCode());

            // Проверяем тело ответа
            String actualMessage = duplicateResponse.jsonPath().getString("message");
            String expectedMessage = "Этот логин уже используется. Попробуйте другой.";
            Assert.assertEquals("Неверное сообщение об ошибке",
                    expectedMessage,
                    actualMessage);

            // Проверяем код ошибки
            int actualCode = duplicateResponse.jsonPath().getInt("code");
            Assert.assertEquals("Неверный код ошибки", Config.STATUS_CODE_CONFLICT, actualCode);

        } finally {
            cleanUp(login, Config.DEFAULT_PASSWORD); // Удаляем созданного курьера, даже если упадет тест
        }
    }

    @Test
    @DisplayName("Создание курьера без логина → 400")
    @Description("Проверка ошибки при отсутствии логина")
    public void createCourierWithoutLogin_ShouldReturn400() {
        String json = "{"
                + "\"password\": \"" + Config.DEFAULT_PASSWORD + "\","
                + "\"firstName\": \"" + Config.DEFAULT_FIRST_NAME + "\""
                + "}";
        Response response = createCourierPartial(json);
        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals(
                "Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message")
        );
    }

    @Test
    @DisplayName("Создание курьера без пароля → 400")
    public void createCourierWithoutPassword_ShouldReturn400() {
        String json = "{"
                + "\"login\": \"" + Config.DEFAULT_COURIER_LOGIN_PREFIX + "\","
                + "\"firstName\": \"" + Config.DEFAULT_FIRST_NAME + "\""
                + "}";
        Response response = createCourierPartial(json);
        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertEquals(
                "Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message")
        );

        Assert.assertEquals(400, response.getStatusCode());
    }
    @Test
    @DisplayName("Создание курьера без имени → 400")
    public void createCourierWithoutFirstName_ShouldReturn400() {
        String json = "{"
                + "\"login\": \"" + Config.DEFAULT_COURIER_LOGIN_PREFIX + "\","
                + "\"password\": \"" + Config.DEFAULT_PASSWORD + "\""
                + "}";
        Response response = createCourierPartial(json);
        //Assert.assertEquals(400, response.getStatusCode());
        try {
            // 3. Проверяем ошибку
            Assert.assertEquals(
                    "Недостаточно данных для создания учетной записи",
                    response.jsonPath().getString("message")
            );
            Assert.assertEquals(400, response.getStatusCode());
        } finally {
            // 4. Пытаемся удалить на случай, если курьер создался
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201 || response.getStatusCode() == 409) {
                cleanUp(Config.DEFAULT_COURIER_LOGIN_PREFIX, Config.DEFAULT_PASSWORD);
            }


            Assert.assertEquals(400, response.getStatusCode());
        }
    }

}


