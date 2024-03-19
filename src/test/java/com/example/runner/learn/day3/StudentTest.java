package com.example.runner.learn.day3;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
@Log4j2
class StudentTest {
    @Test
    public void builderPattern1() {
        Student student = Student.builder()
                .name("홍길동")
                .grade(1)
                .build();
        log.info(student);
    }
}