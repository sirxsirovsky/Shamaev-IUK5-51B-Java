package task3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestaurantService {
    private final Map<String, Integer> ingredientsStock = new ConcurrentHashMap<>();

    public RestaurantService() {
        ingredientsStock.put("Тесто для пиццы", 10);
        ingredientsStock.put("Сыр", 20);
        ingredientsStock.put("Томатный соус", 15);
        ingredientsStock.put("Пепперони", 8);
        ingredientsStock.put("Салат", 30);
    }

    public Order checkIngredients(Order order) {
        System.out.println("1. Проверка ингредиентов для: " + order.getDishName());

        if (order.getDishName().equalsIgnoreCase("Пицца Пепперони")) {
            synchronized (ingredientsStock) {
                if (ingredientsStock.getOrDefault("Тесто для пиццы", 0) > 0 &&
                        ingredientsStock.getOrDefault("Сыр", 0) > 0 &&
                        ingredientsStock.getOrDefault("Томатный соус", 0) > 0 &&
                        ingredientsStock.getOrDefault("Пепперони", 0) > 0) {

                    ingredientsStock.computeIfPresent("Тесто для пиццы", (k, v) -> v - 1);
                    ingredientsStock.computeIfPresent("Сыр", (k, v) -> v - 1);
                    ingredientsStock.computeIfPresent("Томатный соус", (k, v) -> v - 1);
                    ingredientsStock.computeIfPresent("Пепперони", (k, v) -> v - 1);

                    System.out.println("   Ингредиенты для '" + order.getDishName() + "' в наличии. Списаны со склада.");
                    return order;
                } else {
                    throw new RuntimeException("Недостаточно ингредиентов для '" + order.getDishName() + "'");
                }
            }
        }
        return order;
    }

    public Order cook(Order order) {
        System.out.println("2. Началось приготовление: " + order.getDishName() + ". Займет " + order.getCookingTimeMs() + " мс.");
        try {
            Thread.sleep(order.getCookingTimeMs());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Процесс приготовления был прерван", e);
        }
        System.out.println("   " + order.getDishName() + " готово!");
        return order;
    }

    public Order calculateCost(Order order) {
        System.out.println("3. Расчет стоимости для: " + order.getDishName());
        if (order.getPrice() > 500) {
            double newPrice = order.getPrice() * 0.90;
            System.out.printf("   Применена скидка 10%%. Старая цена: %.2f, новая цена: %.2f\n", order.getPrice(), newPrice);
            order.setPrice(newPrice);
        } else {
            System.out.println("   Стоимость заказа не превышает 500. Скидка не применяется.");
        }
        return order;
    }

    public void notifyReady(Order order) {
        System.out.printf("4. Уведомление: Ваш заказ '%s' готов! Итоговая стоимость: %.2f руб.\n",
                order.getDishName(), order.getPrice());
    }
}