package com.example.learner.domain.course.application;

import com.example.learner.domain.course.dao.CourseRepository;
import com.example.learner.domain.course.decorator.CreateCoursesTimerWrapper;
import com.example.learner.domain.course.decorator.DistributionSummaryWrapper;
import com.example.learner.domain.course.entity.Course;
import com.example.learner.domain.course.event.CreateCourseEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceIml implements CourseService {
    private final CourseRepository courseRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DistributionSummaryWrapper distributionSummaryWrapper;
    private final CreateCoursesTimerWrapper createCoursesTimerWrapper;

    @SneakyThrows
    public Course createCourse(Course course) {
        // Increment Course via Event
        eventPublisher.publishEvent(new CreateCourseEvent(this));
        // DistributionSummary
        distributionSummaryWrapper.record(course.getRating());
        // Timer
        return createCoursesTimerWrapper.recordCallable(() -> courseRepository.save(course));
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
