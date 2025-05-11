package model;

import java.util.List;

// Модель данных для запроса на создание заказа
public class OrderRequest {
    private String firstName; // обязательный
    private String lastName; // обязательный
    private String address; // обязательный
    private String metroStation; // обязательный
    private String phone; // обязательный
    private String deliveryDate; // обязательный
    private String comment; // обязательный
    private List<String> color; // необязательный
    private int rentTime; // обязательный

    // Конструктор со всеми нужными полями
    public OrderRequest(String firstName, String lastName, String address, String metroStation,
                        String phone, String deliveryDate, String comment, List<String> color, int rentTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
        this.rentTime = rentTime;
    }

    // Пустой конструктор нужен для сериализации/десериализации
    public OrderRequest() {}

    // Геттеры и сеттеры
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMetroStation() { return metroStation; }
    public void setMetroStation(String metroStation) { this.metroStation = metroStation; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public List<String> getColor() { return color; }
    public void setColor(List<String> color) { this.color = color; }

    public int getRentTime() { return rentTime; }
    public void setRentTime(int rentTime) { this.rentTime = rentTime; }
}