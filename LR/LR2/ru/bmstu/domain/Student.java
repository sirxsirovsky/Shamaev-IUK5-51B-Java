package ru.bmstu.domain;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


public class Student extends Person {

    private String group;
    private int course;
    private final Map<String, Integer> subjects;

    public Student(String firstName, String secondName, int age, String group, int course) {
        super(firstName, secondName, age);

        this.group = group;
        this.course = course;
        this.subjects = new TreeMap<>(Comparator.reverseOrder());

    }

    public void addSubject(String name, int grade) {
        this.subjects.put(name, grade);
    }


    public Map<String, Integer> getSubjects() {
        return subjects;
    }

    public double getAverageGrade() {
        if (subjects.isEmpty()) {
            return 0.0;
        }
        return subjects.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .getAsDouble();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }
}