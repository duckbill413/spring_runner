package com.example.learner.domain.course.application;

import com.example.learner.domain.course.entity.Course;

import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);

    Optional<Course> findCourseById(Long courseId);

    Iterable<Course> findAllCourses();

    Course updateCourse(Course course);

    void deleteCourseById(Long courseId);
    long count();
}
