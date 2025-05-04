import io.qameta.allure.*;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import org.junit.Test;// импортируем Test
import static steps.OrderSteps.*;  // шаги по заказу
import config.Config;
import io.qameta.allure.junit4.DisplayName; // импорт DisplayName
import org.junit.FixMethodOrder; //упорядочивние тестов в аллюр
import org.junit.runners.MethodSorters;
import java.util.Map;


@Epic("API Курьеров")
@Feature("Логин курьера")

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreatingAnOrderTest {

    @Test
    @DisplayName("#1 - Создание заказа")
    @Story("Создаем заказы с вариантами указания цвета самоката: черный, серый, черный или серый, не указывая цвет")
    @Description("Проверка , что: заказ создается, API возвращает статус 201, в теле ответа \" track: \"")

    public void test1_CreatingAnOrder() {
        // Создаем данные для заказа с заданным цветом
        Map<String, Object> orderData = getBaseOrderData();
        orderData.put("color", new String[]{"BLACK"}); // Указываем нужный цвет

        Response orderResponse = createOrderNew(orderData);

        int statusCode = orderResponse.getStatusCode();                    // Получаем код ответа и тело ответа
        String responseBody = orderResponse.getBody().asString();
       try {
            // Проверка тела ответа
            assertFalse("Тело ответа пустое", responseBody.isEmpty());

            // Проверяем наличие и непустое значение track
            assertTrue("Ответ не содержит поле 'track'", responseBody.contains("\"track\""));

            // Извлекаем track из JSON
            int track = loggedResponse.jsonPath().getInt("track");

            // Проверяем, что id не пустое (считаем что 0 недопустим)
            assertNotEquals(0, track); // Если 0 недопустим

            // проверяем код ответа на 201
            assertEquals("Неверный статус код при авторизации курьера",
                    Config.STATUS_CODE_CREATED,
                    statusCode);
        } finally {
//             // Удаляем заказ даже если упадет тест. НЕТ ТАКОЙ РУЧКИМ НА УДАЛЕНИЕ ЗАКАЗА
        }
    }

}
