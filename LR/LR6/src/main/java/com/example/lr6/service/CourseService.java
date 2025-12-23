package com.example.lr6.service;

import com.example.lr6.model.Course;
import com.example.lr6.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course existingCourse = courseOptional.get();
            existingCourse.setTitle(courseDetails.getTitle());
            existingCourse.setDescription(courseDetails.getDescription());
            return courseRepository.save(existingCourse);
        }
        return null;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}