package com.example.learner.domain.course.application;

import com.example.learner.domain.course.dao.CourseRepository;
import com.example.learner.domain.course.entity.Course;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final CourseRepository courseRepository;
    private final Counter createCourseCounter;
    private final Timer createCoursesTimer;
    private final DistributionSummary distributionSummary;

    @SneakyThrows
    public Course createCourse(Course course) {
        createCourseCounter.increment();
        // Counter
//        return courseRepository.save(course);
        // DistributionSummary
        distributionSummary.record(course.getRating());
        // Timer
        return createCoursesTimer.recordCallable(() -> courseRepository.save(course));
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

    @Override
    public long count() {
        return courseRepository.count();
    }
}
