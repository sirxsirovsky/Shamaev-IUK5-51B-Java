package ru.bmstu.service;

import ru.bmstu.domain.Person;

public class PersonPrinter {

    public static void printFI(Person p) {
        System.out.println("Имя и фамилия: " + p.getFirstName() + " " + p.getSecondName());
    }

    public static void printInfo(Person p) {
        System.out.println("--- Полная информация ---");
        System.out.println("Имя: " + p.getFirstName());
        System.out.println("Фамилия: " + p.getSecondName());
        System.out.println("Возраст: " + p.getAge());

        if (p.getPhone() != null && !p.getPhone().isEmpty()) {
            System.out.println("Телефон: " + p.getPhone());
        } else {
            System.out.println("Телефон: не указан");
        }
        System.out.println("-------------------------");
    }
}
