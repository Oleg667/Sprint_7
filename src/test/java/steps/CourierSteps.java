package steps;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import io.restassured.config.SSLConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import config.Config;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;



public class CourierSteps {

    static {
        RestAssured.baseURI = Config.BASE_URI; //Устанавливаем базовый URL один раз при загрузке класса
        RestAssured.config = RestAssured.config()
                .sslConfig(new SSLConfig().relaxedHTTPSValidation())
                .httpClient(HttpClientConfig.httpClientConfig() //Получить конфигурацию для HttpClient
                .setParam("http.connection.timeout", 5000) //сколько максимум ждать подключения к серверу
                .setParam("http.socket.timeout", 5000) //сколько ждать ответа после подключения
                );
    }

    // Метод генерации уникального логина
    @Step("Генерация уникального логина для курьера")
    public static String generateUniqueLogin() {
        String login = config.Config.DEFAULT_COURIER_LOGIN_PREFIX + System.currentTimeMillis();
        System.out.println("Сгенерирован уникальный логин: " + login);
        return login;
    }

    // Метод создания курьера
    @Step("Создание курьера (логин: {login})")
    public static Response createCourier(String login, String password, String firstName) {
        String requestBody = String.format(
                "{ \"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\" }",
                login, password, firstName
        );

        System.out.println("Отправка запроса на создание курьера...");

        return given()
                //.log().all()  // ← Логирует ВЕСЬ запрос (URL, headers, body)
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                //.post("/api/v1/courier");
                .post(Config.COURIER_API);
    }
    @Step("создание курьера с неполными данными {jsonRequestBody}")
    // Метод для тестирования неполных данных при создании курьера
    public static Response createCourierPartial(String jsonRequestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(jsonRequestBody)
                .post(Config.COURIER_API);
    }
    // Метод авторизации курьера, возвращающий полный Response
    @Step("Авторизация курьера по логину: {login}")
    public static Response courierLogged(String login, String password) {
        String requestBody = String.format(
                "{ \"login\": \"%s\", \"password\": \"%s\" }",
                login, password
        );

        return given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(Config.COURIER_LOGIN_API);
    }
    @Step("авторизация курьера с неполными данными {jsonRequestBody}")
    // Метод для тестирования неполных данных при авторизации курьера
    public static Response courierLoggedPartial(String jsonRequestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(jsonRequestBody)
                .post(Config.COURIER_LOGIN_API);
    }

    // Метод получения ID курьера через авторизацию
    @Step("Получение ID курьера по логину: {login} и паролю")
    public static String getCourierId(String login, String password) {
        String requestBody = String.format(
                "{ \"login\": \"%s\", \"password\": \"%s\" }",
                login, password
        );

        Response response = given()
                .header("Content-type", "application/json")
                .body(requestBody)
                .when()
                .post(Config.COURIER_LOGIN_API);

        System.out.println("Получен ID курьера для логина: " + login);
        return response.jsonPath().getString("id");
    }

    // Метод удаления курьера
    @Step("Удаление курьера (ID: {courierId})")
    public static void deleteCourier(String courierId) {
        System.out.println("Удаление курьера с ID: " + courierId);

        given()
                .header("Content-type", "application/json")
                .when()
                .delete(Config.COURIER_DEL_API + courierId)
                .then()
                .statusCode(Config.STATUS_CODE_OK);

        System.out.println("Курьер с ID " + courierId + " успешно удалён");
    }

//    // Метод проверки успешного создания курьера (201 статус).... перенес в тест проверку. ПОТОМ УДАЛИТЬ!!!!
//    @Step("Проверка успешного создания курьера (код 201)")
//    public static void verifyCreation(int statusCode, String responseBody) {
//        System.out.println("Код ответа: " + statusCode);
//        System.out.println("Тело ответа: " + responseBody);
//
//        if (statusCode != Config.STATUS_CODE_CREATED) {
//            Assert.fail("Ошибка создания курьера. Код: " + statusCode + ", Ответ: " + responseBody);
//        }
//        System.out.println("Курьер успешно создан.");
//    }

    // Метод очистки тестовых данных курьера
    @Step("Очистка тестовых данных, удаление курьера проверка  удаления")
    public static void cleanUp(String login, String password) {
        try {
            String courierId = getCourierId(login, password);
            if (courierId != null) {
                deleteCourier(courierId);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении курьера: " + e.getMessage());
        }
    }
}