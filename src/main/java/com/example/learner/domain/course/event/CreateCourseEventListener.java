package com.example.learner.domain.course.event;

import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCourseEventListener implements ApplicationListener<CreateCourseEvent> {
    private final Counter createCourseCounter;
    @Override
    public void onApplicationEvent(CreateCourseEvent event) {
        createCourseCounter.increment();
    }
}
