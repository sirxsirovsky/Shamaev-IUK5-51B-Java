package ru.bmstu.service;

import ru.bmstu.domain.Person;
import ru.bmstu.domain.Student;


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

    public static void printStudentInfoWithSubjects(Student student) {
        System.out.println("--- Полная информация о студенте ---");
        System.out.println("Имя: " + student.getFirstName());
        System.out.println("Фамилия: " + student.getSecondName());
        System.out.println("Группа: " + student.getGroup());
        System.out.println("Средний балл: " + String.format("%.2f", student.getAverageGrade()));

        if (student.getSubjects().isEmpty()) {
            System.out.println("Дисциплины: не указаны.");
        } else {
            System.out.println("Дисциплины (отсортированы в обратном порядке):");
            student.getSubjects().forEach((subject, grade) ->
                    System.out.println("\t- " + subject + ": " + grade)
            );
        }
        System.out.println("------------------------------------");
    }

}
