package task3;

public class Order {
    private final String dishName;
    private final int cookingTimeMs;
    private double price;

    public Order(String dishName, int cookingTimeMs, double price) {
        this.dishName = dishName;
        this.cookingTimeMs = cookingTimeMs;
        this.price = price;
    }

    public String getDishName() { return dishName; }
    public int getCookingTimeMs() { return cookingTimeMs; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Заказ{" +
                "Блюдо='" + dishName + '\'' +
                ", цена=" + price +
                '}';
    }
}
