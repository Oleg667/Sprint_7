package factory;

import model.OrderRequest;
import java.util.List;

public class OrderFactory {
// шаблон заказа с данными
    public static OrderRequest createDefaultOrder() {
        return new OrderRequest(
                "Naruto", "Uchiha", "Konoha, 142 apt.", "4",
                "+7 800 355 35 35", "2020-06-06",
                "Saske, come back to Konoha",
                List.of("BLACK"), 5
        );
    }
// шаблон для параметризации тестов по полю color
    public static OrderRequest createOrderWithColors(List<String> colors) {
        return new OrderRequest(
                "Naruto", "Uchiha", "Konoha, 142 apt.", "4",
                "+7 800 355 35 35", "2020-06-06",
                "Saske, come back to Konoha",
                colors, 5
        );
    }
}
