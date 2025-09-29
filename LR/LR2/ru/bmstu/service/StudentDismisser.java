package ru.bmstu.service;

import ru.bmstu.domain.Person;
import ru.bmstu.domain.Student;

public class StudentDismisser implements Dismissable {

    @Override
    public void dismiss(Person p) {
        System.out.println("\n=== Процедура отчисления ===");

        if (p instanceof Student) {
            //Student student = (Student) p;
            System.out.println("Отчислен студент:");
            System.out.println("Имя: " + p.getFirstName());
            System.out.println("Фамилия: " + p.getSecondName());
            System.out.println("Группа: " + ((Student) p).getGroup());
            System.out.println("Курс: " + ((Student) p).getCourse());

        } else {
            System.out.println("Ошибка: переданный объект не является студентом.");
        }
        System.out.println("============================");
    }
}