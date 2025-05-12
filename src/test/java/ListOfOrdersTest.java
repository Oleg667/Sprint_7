import io.qameta.allure.*;
import io.restassured.response.Response;
import static org.junit.Assert.*;
import org.junit.Test;
import static steps.OrderSteps.*;
import config.Config;
import io.qameta.allure.junit4.DisplayName;
import factory.OrderFactory;

@Epic("API Заказа")
@Feature("Создание заказа")

public class ListOfOrdersTest {



    @Test
    @DisplayName("#1 - получение списка заказов")
    @Story("Получаем список заказов и проверяем что у ответа есть тело и основные блоки")
    @Description("Проверка список заказов получен, API возвращает статус 200, наличие в теле ответа не пустых блоков \" orders\", \"pageInfo\",\"availableStations\" ")

    public void test1_listOfOrdersTest() {
        //Создать новый заказ на случай если база пустая
        Response orderResponse = createOrderNew(OrderFactory.createDefaultOrder());
        // пример запроса с данными listOfOrder(123, "[\"1\", \"2\"]", 30, 0)
        Response orderListResponse = listOfOrder(null, null, null, null);

        int statusCode = orderListResponse.getStatusCode();                    // Получаем код ответа и тело ответа
        String responseBody = orderListResponse.getBody().asString();
        try {
            // Проверка тела ответа
            assertFalse("Тело ответа пустое", responseBody.isEmpty());

            // Проверка наличия блока 'orders' и непустой
            assertTrue("Ответ не содержит блока 'orders'", responseBody.contains("\"orders\""));
            assertFalse("Блок 'orders' пустой", orderListResponse.jsonPath().getList("orders").isEmpty());

            // Проверка наличия блока 'pageInfo' и непустой
            assertTrue("Ответ не содержит блока 'pageInfo'", responseBody.contains("\"pageInfo\""));
            assertFalse("Блок 'pageInfo' пустой", orderListResponse.jsonPath().getMap("pageInfo").isEmpty());

            // Проверка наличия блока 'availableStations' и непустой
            assertTrue("Ответ не содержит блока 'availableStations'", responseBody.contains("\"availableStations\""));
            assertFalse("Блок 'availableStations' пустой", orderListResponse.jsonPath().getList("availableStations").isEmpty());
            // проверяем код ответа на 201
            assertEquals("Неверный статус код при создании заказа",
                    Config.STATUS_CODE_OK,
                    statusCode);
        } finally {
//              Удаляем заказ даже если упадет тест. НЕТ ТАКОЙ РУЧКИ НА УДАЛЕНИЕ ЗАКАЗА
        }
    }

}

