package ru.bmstu.service;

import ru.bmstu.domain.Person;
import ru.bmstu.domain.Student;

public class PersonDemo {
    private final Person person;

    public PersonDemo(Person person) {
        this.person = person;
    }

    public void demo() {
        System.out.println("=== Демонстрация работы с Person ===");
        PersonPrinter.printFI(this.person);
        PersonPrinter.printInfo(this.person);
    }

    public static void demonstrateStudent() {
        Student student = new Student("Петр", "Петров", 20, "ИУК5-31Б", 2);

        System.out.println("\n=== Демонстрация работы со Student ===");
        PersonPrinter.printInfo(student);

        Dismissable dismisser = new StudentDismisser();
        dismisser.dismiss(student);
    }
}
