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

    public static void demonstrateManager() {
        System.out.println("\n\n=== Демонстрация работы StudentManager ===");
        StudentManager manager = new StudentManager();

        // Создаем и добавляем студентов
        Student petr = new Student("Петр", "Петров", 20, "ИУК5-31Б", 2);
        petr.addSubject("Физика", 5);
        petr.addSubject("Математика", 4);
        petr.addSubject("Алгоритмы", 5);

        Student anna = new Student("Анна", "Сидорова", 19, "ИУК5-21Б", 1);
        anna.addSubject("Программирование", 5);
        anna.addSubject("История", 3);

        manager.addStudent(petr);
        manager.addStudent(anna);
        manager.printAllStudents();

        // Поиск студентов
        System.out.println("\n--- Поиск студентов ---");
        System.out.println("Ищем студента по имени 'Анна': " + manager.findByName("Анна").getFirstName());
        System.out.println("Ищем студентов со средним баллом выше 4.5:");
        manager.findByAverageGrade(4.5).forEach(s -> System.out.println("\t- " + s.getFirstName()));

        // Удаление студента
        System.out.println("\n--- Удаление студента ---");
        manager.removeStudent("Петр");
        manager.printAllStudents();
    }

}
