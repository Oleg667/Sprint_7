import io.qameta.allure.*;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import org.junit.Test;
import static steps.OrderSteps.*;
import config.Config;
import io.qameta.allure.junit4.DisplayName;
import model.OrderRequest;
import model.OrderResponse;
import factory.OrderFactory;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

@Epic("API Заказа")
@Feature("Создание заказа")
@RunWith(Parameterized.class)

public class CreatingAnOrderTest {

    private final List<String> color;  // параметр теста

    public CreatingAnOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {Collections.singletonList("BLACK")},
                {Collections.singletonList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}, //отсутствует параметр
                {null}  // цвет не указан
        });
    }

    @Test
    @DisplayName("Создание заказа {color}")
    @Story("Создаем заказы с вариантами указания цвета самоката: черный, серый, черный или серый, не указывая цвет, цвет null")
    @Description("Проверка , что: заказ создается, API возвращает статус 201, наличие в теле ответа не пустого \" track: \"")

    public void test1_CreatingAnOrder_Color() {

        logOrderCreationWithColor(color);// отображение в Allure цвета

        OrderRequest order = OrderFactory.createOrderWithColors(color);
        Response orderResponse = createOrderNew(order);

        int statusCode = orderResponse.getStatusCode();                    // Получаем код ответа и тело ответа
        String responseBody = orderResponse.getBody().asString();
        OrderResponse orderResponseModel = orderResponse.as(OrderResponse.class);
       try {
            // Проверка тела ответа
            assertFalse("Тело ответа пустое", responseBody.isEmpty());

            // Проверяем наличие и непустое значение track
            assertTrue("Ответ не содержит поле 'track'", responseBody.contains("\"track\""));

            // Извлекаем track из модели
           int track = orderResponseModel.getTrack();

           // Проверяем, что track не пустое (считаем что 0 недопустим)
            assertNotEquals(0, track);

            // проверяем код ответа на 201
            assertEquals("Неверный статус код при создании заказа",
                    Config.STATUS_CODE_CREATED,
                    statusCode);
        } finally {
//             // Удаляем заказ даже если упадет тест. НЕТ ТАКОЙ РУЧКИ НА УДАЛЕНИЕ ЗАКАЗА
        }
    }

}
