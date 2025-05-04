import config.Config;
import io.restassured.response.Response;
import org.junit.Assert;  // Для JUnit 4
import org.junit.Test;// импортируем Test
import static steps.CourierSteps.*;  // или import steps.CounterSteps;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;// импортируем RestAssured
import org.junit.Before;// импортируем Before
import io.qameta.allure.junit4.DisplayName; // импорт DisplayName
import io.qameta.allure.Description; // импорт Description


public class CreateCourierTest {

 @Test // Основной тестовый метод
 @DisplayName("Создание тестового курьера")
 @Description("Тест проверяет корректность API при создании нового курьера. Ожидаемый результат:** HTTP 201 и JSON с ID курьера.")
    public void Creating_Courier_New() {

        String login = generateUniqueLogin(); // Генерация уникального логина для каждого теста

        Response createResponse = createCourier(login, Config.DEFAULT_PASSWORD, Config.DEFAULT_FIRST_NAME); // Отправляем запрос на создание курьера
        int statusCode = createResponse.getStatusCode();                    // Получаем код ответа и тело ответа
        String responseBody = createResponse.getBody().asString();

        // Блок try-finally гарантирует очистку данных даже при падении теста
        try {
            verifyCreation(statusCode, responseBody);// Проверяем успешность создания курьера
            String courierId = getCourierId(login, Config.DEFAULT_PASSWORD);  // Получаем ID созданного курьера для последующего удаления
            System.out.println("ID курьера для удаления: " + courierId);

        } finally {
            // Очищаем тестовые данные
            cleanUp(login, Config.DEFAULT_PASSWORD);
        }
    }

}