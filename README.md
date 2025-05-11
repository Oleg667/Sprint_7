# Sprint_7
Учебный проект по автотестированию API для приложения по заказу самокатов Яндекс.Самокат.

Описание
Версия Java 11.

Проект использует следующие библиотеки:
JUnit 4
RestAssured
Allure
Документация
Ссылка на документацию учебного сервиса Яндекс.Самокат. https://qa-scooter.praktikum-services.ru/docs/#api-Orders-GetOrdersPageByPage
Ссылка на учебное приложение Яндекс.Самокат. https://qa-scooter.praktikum-services.ru/

Запуск автотестов
Для запуска автотестов необходимо:

Скачать код
git clone https://github.com/Oleg667/Sprint_7
Запустить команду в проекте
mvn clean test
Для создания отчета в Allure ввести команду
mvn allure:report
Структура проекта
├───src

│   ├───main

│   │   ├───java

│   │   └───resources

│   └───test

│       └───java

│           │   CourierLoggedTest.java

│           │   CreateCourierTest.java

│           │   CreatingAnOrderTest.java

│           │   ListOfOrdersTest.java

│           │   

│           ├───config

│           │       Config.java

│           │       

│           ├───factory

│           │       OrderFactory.java

│           │       

│           ├───model

│           │       OrderRequest.java

│           │       OrderResponse.java

│           │       

│           └───steps

│                   CourierSteps.java

│                   OrderSteps.java

