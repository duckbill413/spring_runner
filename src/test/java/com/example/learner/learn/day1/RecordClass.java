package com.example.learner.learn.day1;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
public class RecordClass {
    @Test
    @DisplayName("Record Class Test")
    public void recordClassTest() {
        Person p = new Person("김싸피", 21);
        PersonRecord pr = new PersonRecord("김싸피", 21);

        log.info("==========Person class============");
        log.info("ToString: {}", p);
        log.info("{} {}", p.getName(), p.getAge());
        log.info("==========Person Record============");
        log.info("ToString: {}", pr);
        log.info("{} {}", pr.name(), pr.age());
    }
}
