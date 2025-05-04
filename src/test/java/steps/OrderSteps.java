package steps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import io.restassured.config.SSLConfig;
import io.restassured.config.HttpClientConfig;
import config.Config;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;


public class OrderSteps {

    static {
        RestAssured.baseURI = Config.BASE_URI; //Устанавливаем базовый URL один раз при загрузке класса
        RestAssured.config = RestAssured.config()
                .sslConfig(new SSLConfig().relaxedHTTPSValidation())
                .httpClient(HttpClientConfig.httpClientConfig() //Получить конфигурацию для HttpClient
                        .setParam("http.connection.timeout", 5000) //сколько максимум ждать подключения к серверу
                        .setParam("http.socket.timeout", 5000) //сколько ждать ответа после подключения
                );
    }
    // Метод для получения стандартных данных заказа
    public static Map<String, Object> getBaseOrderData() {
        Map<String, Object> order = new HashMap<>();
        order.put("firstName", "Имя_заказчика");
        order.put("lastName", "Uchiha");
        order.put("address", "Konoha, 142 apt.");
        order.put("metroStation", 4);
        order.put("phone", "+7 800 355 35 35");
        order.put("rentTime", 5);
        order.put("deliveryDate", "2020-06-06");
        order.put("comment", "Saske, come back to Konoha");
        order.put("color", new String[]{}); // по умолчанию пустой массив
        return order;
    }

    // Метод создания нового заказа
    @Step("Создание заказа ")
    public static Response createOrderNew(Map<String, Object> orderData) {
        // Преобразуем Map в JSON строку с помощью Gson
        String requestBody = new Gson().toJson(orderData);

        System.out.println("Отправка запроса на создание Заказа");

        return given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .log().body()  // Логирование тела запроса, вывод в консоль во время теста
                .post(Config.CREATING_AN_ORDER_API);
    }
}