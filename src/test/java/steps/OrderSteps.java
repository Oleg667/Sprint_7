package steps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import io.restassured.config.SSLConfig;
import io.restassured.config.HttpClientConfig;
import config.Config;
import model.OrderRequest;
import io.restassured.specification.RequestSpecification;
import java.util.List;


public class OrderSteps {

    static {
        RestAssured.baseURI = Config.BASE_URI; //Устанавливаем базовый URL один раз при загрузке класса
        RestAssured.config = RestAssured.config()
                .sslConfig(new SSLConfig().relaxedHTTPSValidation())
                .httpClient(HttpClientConfig.httpClientConfig() //Получить конфигурацию для HttpClient
                        .setParam("http.connection.timeout", Config.CONNECTION_TIMEOUT_MS) //сколько максимум ждать подключения к серверу
                        .setParam("http.socket.timeout", Config.SOCKET_TIMEOUT_MS) //сколько ждать ответа после подключения
                );
    }

    // Метод создания нового заказа
    @Step("Создание заказа ")
    public static Response createOrderNew(OrderRequest orderData) {

        System.out.println("Отправка запроса на создание Заказа");

        return given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                //.log().body()  // Логирование тела запроса, вывод в консоль во время теста
                .post(Config.CREATING_AN_ORDER_API);
    }

    // Метод шага для Allure, чтобы записать цвет самоката в отчет
    @Step("Создание заказа с цветом самоката: {color}")
    public static void logOrderCreationWithColor(List<String> color) {
        System.out.println("Создаем заказ с цветом: " + color);
    }


    // Метод получения списка заказов с разными параметрами
    @Step("Получение списка заказа  ")
    public static Response listOfOrder(Integer courierId, String nearestStation, Integer limit, Integer page) {

        System.out.println("Отправка запроса на получение списка заказов");

        RequestSpecification request = given()
                .baseUri(Config.BASE_URI)
                .header("Content-type", "application/json");

        // Добавляем только те параметры, которые не равны null
        if (courierId != null) {
            request.queryParam("courierId", courierId);
        }
        if (nearestStation != null) {
            request.queryParam("nearestStation", nearestStation);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }
        if (page != null) {
            request.queryParam("page", page);
        }
        return request
                .when()
                .get(Config.LIST_OF_ORDER_API);
    }


}