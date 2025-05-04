import config.Config;
import io.restassured.response.Response;
import org.junit.Assert;  // Для JUnit 4
import org.junit.Test;// импортируем Test
import static steps.CourierSteps.*;  // или import steps.CounterSteps;
import static config.Config.*;
//import io.qameta.allure.restassured.AllureRestAssured;
//import io.qameta.allure.Description;
//import io.qameta.allure.Step;
//import io.qameta.allure.junit4.AllureJunit4;
import org.junit.runner.RunWith;
import io.restassured.RestAssured;// импортируем RestAssured
import org.junit.Before;// импортируем Before

public class CreateCourierTest {
// Подключение Allure логирования к каждому запросу
// @Before
//    public void setUp() {
//        // Включаем логирование в Allure
//        RestAssured.filters(new AllureRestAssured());
//    }
    // Основной тестовый метод
 @Test
    // Задаем читаемое имя теста в отчете
 //@Description("Создание и удаление тестового курьера")
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