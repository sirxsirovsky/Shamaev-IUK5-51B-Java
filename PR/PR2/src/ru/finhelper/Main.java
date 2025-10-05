package src.ru.finhelper;

import src.ru.finhelper.service.*;
import src.ru.finhelper.ui.ConsoleMenu;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        System.out.println("Добро пожаловать в Финансовый Помощник!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите формат для хранения данных:");
        System.out.println("1. Текстовый файл (простой)");
        System.out.println("2. JSON файл (рекомендуется)");
        System.out.print("➡️ Ваш выбор: ");

        String choice = scanner.nextLine();
        DataStorage storage;

        if ("1".equals(choice)) {
            System.out.println("Выбран текстовый формат.");
            storage = new FileStorage("data/storage.txt");
        } else {
            System.out.println("Выбран формат JSON.");
            storage = new JsonStorage(
                    "data/transactions.json",
                    "data/scheduled_payments.json"
            );
        }

        TransactionManager manager = new TransactionManager(storage);
        ConsoleMenu menu = new ConsoleMenu(manager);
        menu.run();
    }
}