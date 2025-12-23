package com.example.lr6;

import com.example.lr6.model.Course;
import com.example.lr6.model.Student;
import com.example.lr6.repository.CourseRepository;
import com.example.lr6.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public DataLoader(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        studentRepository.save(new Student(null, "Ivan Ivanov", "ivan@test.com"));
        studentRepository.save(new Student(null, "Petr Petrov", "petr@test.com"));

        courseRepository.save(new Course(null, "Java Spring", "Backend Development"));
        courseRepository.save(new Course(null, "1C Programming", "Enterprise Automation"));
    }
}