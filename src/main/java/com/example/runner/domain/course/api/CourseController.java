package com.example.runner.domain.course.api;

import com.example.runner.domain.course.application.CourseService;
import com.example.runner.domain.course.entity.Course;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @GetMapping("/index")
    public List<Course> index() {
        List<Course> courseList = (List<Course>) courseService.findAllCourses();
        return courseList.isEmpty() ? Collections.EMPTY_LIST : courseList;
    }

    @PostMapping("/addcourse")
    public Iterable<Course> addCourse(@Valid @RequestBody Course course) {
        courseService.createCourse(course);
        return courseService.findAllCourses();
    }

    @GetMapping("/update/{id}")
    public Course showUpdateCourseForm(@PathVariable("id") Long id) {
        return courseService.findCourseById(id).get();
    }

    @PutMapping("/update/{id}")
    public Iterable<Course> updateCourse(@PathVariable("id") Long id, @Valid @RequestBody Course course) {
        course.setId(id);
        courseService.updateCourse(course);
        return courseService.findAllCourses();
    }

    @DeleteMapping("/delete/{id}")
    public Iterable<Course> deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourseById(id);
        return courseService.findAllCourses();
    }
}
