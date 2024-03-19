package com.example.runner.learn.day1;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
public class MultiLineStringTest {
    @Test
    @DisplayName("MultiLine String test")
    public void multiLineStringTest() {
        String multiString = """
                 안녕!
                 난 싸피인
                 야호!!
                """;
        System.out.println(multiString);

        Assertions.assertEquals(" 안녕!\n" +
                        " 난 싸피인\n" +
                        " 야호!!\n",
                multiString);

        var varString = multiString; // 컴파일 단계에서 type 추론
        Assertions.assertTrue(varString instanceof String);
    }
}
