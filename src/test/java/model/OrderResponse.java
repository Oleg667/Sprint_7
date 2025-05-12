package model;

public class OrderResponse {
    private  int track;  // поле сделано final, чтобы оно не менялось

    // Конструктор для инициализации значения track
    public OrderResponse(int track) {
        this.track = track;
    }

    // Публичный конструктор без параметров
    public OrderResponse() {
    }

    // Геттер для получения значения track
    public int getTrack() {
        return track;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "track=" + track +
                '}';
    }
}
