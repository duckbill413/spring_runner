package com.example.runner.domain.course.decorator;

import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@RequiredArgsConstructor
public class CreateCoursesTimerWrapper {
    private final Timer createCoursesTimer;
    @SneakyThrows
    public <T> T recordCallable(Callable<T> callable) {
        return createCoursesTimer.recordCallable(callable);
    }
}
