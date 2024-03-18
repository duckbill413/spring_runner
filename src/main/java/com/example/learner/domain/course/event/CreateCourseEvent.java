package com.example.learner.domain.course.event;

import org.springframework.context.ApplicationEvent;

public class CreateCourseEvent extends ApplicationEvent {
    public CreateCourseEvent(Object source) {
        super(source);
    }
}
