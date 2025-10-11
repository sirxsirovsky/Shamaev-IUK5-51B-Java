package ru.bmstu.service;

import ru.bmstu.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentManager {
    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Добавлен студент: " + student.getFirstName());
    }

    public void removeStudent(String name) {
        boolean removed = students.removeIf(student -> student.getFirstName().equals(name));
        if (removed) {
            System.out.println("Студент " + name + " удален.");
        } else {
            System.out.println("Студент " + name + " не найден.");
        }
    }

    public Student findByName(String name) {
        return students.stream()
                .filter(student -> student.getFirstName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Student> findByAverageGrade(double minAverage) {
        return students.stream()
                .filter(student -> student.getAverageGrade() >= minAverage)
                .collect(Collectors.toList());
    }

    public void printAllStudents() {
        System.out.println("\n--- Список всех студентов ---");
        students.forEach(PersonPrinter::printStudentInfoWithSubjects);
    }
}