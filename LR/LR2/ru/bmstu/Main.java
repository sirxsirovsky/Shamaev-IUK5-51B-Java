package ru.bmstu;

import ru.bmstu.domain.Person;
import ru.bmstu.exception.InvalidPhoneNumberException;
import ru.bmstu.service.PersonDemo;

public class Main {
    public static void main(String[] args) {

        // --- Демонстрация обработки исключений ---
        System.out.println("=== Демонстрация исключений ===");
        Person personWithBadPhone = new Person("Тест", "Тестов", 25);
        try {
            // Пытаемся установить некорректный номер
            personWithBadPhone.setPhone("123");
            System.out.println("Успешно!");
        } catch (InvalidPhoneNumberException e) {
            // Ловим наше исключение
            System.out.println("ОШИБКА: " + e.getMessage());
        }

        try {
            // Пытаемся установить корректный номер
            System.out.print("Устанавливаем корректный номер... ");
            personWithBadPhone.setPhone("+7(999)123-45-67");
            System.out.println("Успешно! Новый номер: " + personWithBadPhone.getPhone());
        } catch (InvalidPhoneNumberException e) {
            System.out.println("ОШИБКА: " + e.getMessage());
        }

        // --- Демонстрация работы StudentManager ---
        PersonDemo.demonstrateManager();
    }
}