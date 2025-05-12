package config;

public class Config {
    // Базовые URL
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    public static final String COURIER_API = "/api/v1/courier";
    public static final String COURIER_LOGIN_API = "/api/v1/courier/login";
    public static final String COURIER_DEL_API = "/api/v1/courier/";
    public static final String CREATING_AN_ORDER_API = "/api/v1/orders";
    public static final String LIST_OF_ORDER_API = "/api/v1/orders";



    // Тестовые данные
    public static final String DEFAULT_COURIER_LOGIN_PREFIX = "auto_test_courier_";
    public static final String DEFAULT_PASSWORD = "667667";
    public static final String DEFAULT_FIRST_NAME = "Oleg";

    // Статус-коды
    public static final int STATUS_CODE_CREATED = 201;
    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_CONFLICT = 409;
    public static final int STATUS_CODE_CLIENT_ERROR = 400;
    public static final int STATUS_CODE_NOT_FOUND = 404;

    // Таймауты
    public static final int CONNECTION_TIMEOUT_MS = 70000;
    public static final int SOCKET_TIMEOUT_MS = 70000;

}