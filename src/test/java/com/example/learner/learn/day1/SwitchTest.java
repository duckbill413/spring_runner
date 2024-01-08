package com.example.learner.learn.day1;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
public class SwitchTest {
    @Test
    @DisplayName("SwitchTest")
    public void switchTest() {
        int num = 85;
        Character score = switch (num / 10) {
            case 10, 9 -> 'A';
            case 8 -> 'B';
            case 7 -> 'C';
            case 6 -> 'D';
            default -> 'F';
        };
        log.info("score 결과값: {}", score);
        Assertions.assertEquals(score, 'B');
    }
}
