package ru.bmstu;

import ru.bmstu.domain.Person;
import ru.bmstu.service.PersonDemo;

public class Main {
    public static void main(String[] args) {
        Person ivan = new Person("Иван", "Иванов", 30, "+7-999-123-45-67");
        PersonDemo personDemo = new PersonDemo(ivan);
        personDemo.demo();

        PersonDemo.demonstrateStudent();
    }
}
