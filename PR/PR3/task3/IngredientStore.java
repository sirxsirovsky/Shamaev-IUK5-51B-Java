package task3;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

class IngredientStore {
    private final Map<String, Integer> inventory = new ConcurrentHashMap<>();

    public IngredientStore() {
        inventory.put("Тесто для пиццы", 10);
        inventory.put("Сыр", 20);
        inventory.put("Томатный соус", 15);
        inventory.put("Пепперони", 10);
        inventory.put("Котлета", 5);
        inventory.put("Булочка", 5);
    }

    public synchronized boolean checkAndUseIngredients(Map<String, Integer> required) {
        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            if (inventory.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                System.out.println("Недостаточно ингредиента: " + entry.getKey());
                return false;
            }
        }
        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            inventory.compute(entry.getKey(), (k, v) -> v - entry.getValue());
        }
        System.out.println("Ингредиенты для заказа списаны. Текущие запасы: " + inventory);
        return true;
    }
}
