package com.example.learner.domain.course.application;

import com.example.learner.domain.course.dao.CourseRepository;
import com.example.learner.domain.course.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final CourseRepository courseRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> findCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Iterable<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourseById(Long courseId) {
        courseRepository.deleteById(courseId);
    }
}
