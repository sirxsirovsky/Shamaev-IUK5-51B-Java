package ru.bmstu;

import ru.bmstu.domain.Person;
import ru.bmstu.exception.InvalidPhoneNumberException;
import ru.bmstu.service.PersonDemo;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Демонстрация исключений ===");
        PersonDemo pd = new PersonDemo(new Person("Тест", "Тестов", 25));
        pd.demo();

        PersonDemo.demonstrateManager();
    }
}