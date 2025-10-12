package task3;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MainTask3 {

    public static void main(String[] args) throws InterruptedException {
        IngredientStore store = new IngredientStore();

        // Заказ 1: ингредиентов должно хватить
        Order pizzaOrder = new Order("Пицца Пепперони", 2000, 600.0);
        Map<String, Integer> pizzaIngredients = Map.of(
                "Тесто для пиццы", 1,
                "Сыр", 2,
                "Томатный соус", 1,
                "Пепперони", 1
        );
        processOrder(pizzaOrder, pizzaIngredients, store);

        // Небольшая пауза между заказами
        Thread.sleep(500);

        // Заказ 2: ингредиентов не хватит (котлет всего 5, а нужно 10)
        Order burgerOrder = new Order("Двойной Бургер", 1500, 450.0);
        Map<String, Integer> burgerIngredients = Map.of(
                "Котлета", 10, // Требуем больше, чем есть в наличии
                "Булочка", 2
        );
        processOrder(burgerOrder, burgerIngredients, store);

        // Ждем завершения асинхронных операций, чтобы увидеть вывод
        Thread.sleep(5000);
    }

    public static void processOrder(Order order, Map<String, Integer> ingredients, IngredientStore store) {
        System.out.println("\n--- Поступил новый заказ: " + order.dishName() + " ---");

        CompletableFuture.supplyAsync(() -> {
            // --- Этап 1: Проверка наличия ингредиентов ---
            System.out.println("1. Проверка ингредиентов для: " + order.dishName());
            if (!store.checkAndUseIngredients(ingredients)) {
                // Если ингредиентов нет, выбрасываем исключение, которое будет поймано в `exceptionally`
                throw new IllegalStateException("Недостаточно ингредиентов для " + order.dishName());
            }
            return order;
        }).thenApplyAsync(o -> {
            // --- Этап 2: Приготовление блюда ---
            System.out.println("2. Начало приготовления: " + o.dishName());
            try {
                Thread.sleep(o.cookingTimeMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   Блюдо '" + o.dishName() + "' готово!");
            return o;
        }).thenApply(o -> {
            // --- Этап 3: Расчёт стоимости ---
            System.out.println("3. Расчет стоимости для: " + o.dishName());
            double finalPrice = o.price();
            if (finalPrice > 500) {
                finalPrice *= 0.90; // Скидка 10%
                System.out.println("   Применена скидка 10%. Итоговая цена: " + finalPrice);
            }
            return "Блюдо '" + o.dishName() + "' готово. Итоговая стоимость: " + finalPrice + " руб.";
        }).thenAccept(result -> {
            // --- Этап 4: Оповещение о готовности ---
            System.out.println("4. [УВЕДОМЛЕНИЕ] " + result);
        }).exceptionally(ex -> {
            // --- Обработка ошибок ---
            System.err.println("ОШИБКА ОБРАБОТКИ ЗАКАЗА: " + ex.getMessage());
            return null; // `exceptionally` должен что-то вернуть
        });
    }
}