package com.example.learner.learn.day1;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
public class StringMethodTest {
    @Test
    @DisplayName("isBlank Test")
    public void isBlankTest() {
        // 문자열이 null인 경우 NullPointerException 발생
        String case1 = null;
        Assertions.assertThrows(NullPointerException.class,
                () -> case1.isBlank(),
                "NullPointError가 발생하지 않았습니다.");

        // 문자열이 빈 경우 true
        String case2 = "";
        Assertions.assertTrue(() -> case2.isBlank());

        // 문자열이 공백인 경우 true
        String case3 = "   ";
        Assertions.assertTrue(() -> case3.isBlank());
    }
    @Test
    @DisplayName("strip test")
    public void stripTest() {
        String s = "    안녕하세요. 난 싸피인   ";

        // strip() test
        Assertions.assertEquals(s.strip(), "안녕하세요. 난 싸피인");
        // stripLeading test
        Assertions.assertEquals(s.stripLeading(), "안녕하세요. 난 싸피인   ");
        // stripTrailing test
        Assertions.assertEquals(s.stripTrailing(), "    안녕하세요. 난 싸피인");
    }
    @Test
    @DisplayName("repeat test")
    public void repeatTest() {
        String s = "안녕! ";
        Assertions.assertEquals(s.repeat(3), "안녕! 안녕! 안녕! ");
    }
}
