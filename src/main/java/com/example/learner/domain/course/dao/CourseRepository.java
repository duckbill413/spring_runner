package com.example.learner.domain.course.dao;

import com.example.learner.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
