package config;

public class Config {
    // Базовые URL
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    public static final String COURIER_API = "/api/v1/courier";
    public static final String COURIER_LOGIN_API = "api/v1/courier/login";
    public static final String COURIER_DEL_API = "api/v1/courier/";

    // Тестовые данные
    public static final String DEFAULT_COURIER_LOGIN_PREFIX = "auto_test_courier_";
    public static final String DEFAULT_PASSWORD = "667667";
    public static final String DEFAULT_FIRST_NAME = "Oleg";

    // Статус-коды
    public static final int STATUS_CODE_CREATED = 201;
    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_CONFLICT = 409;
}