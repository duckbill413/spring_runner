package com.example.learner.domain.menu.dao;

import com.example.learner.domain.menu.domain.Menu;
import com.example.learner.util.MenuFixtureFactory;
import lombok.extern.log4j.Log4j2;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
class MenuJDBCRepositoryImplTest {
    @Autowired
    private MenuJDBCRepository menuJDBCRepository;

    @Test
    @DisplayName("JDBC 메뉴 정보 저장 테스트")
    public void insertMenusTest() {
        // Given
        var parameters = MenuFixtureFactory.getMenuParams();
        var easyRandom = new EasyRandom(parameters);
        List<Menu> menus = IntStream.range(0, 100000)
                .parallel().mapToObj(value -> easyRandom.nextObject(Menu.class))
                .toList();

        log.info("data loaded");
        // Test saveAll
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        menuJDBCRepository.saveAll(menus);
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = afterTime - beforeTime; //두 시간에 차 계산
        log.info("JDBC saveAll 시간차이(ms): " + secDiffTime);

        // Test save one by one
        beforeTime = System.currentTimeMillis();
        menus.forEach(menuJDBCRepository::save);
        afterTime = System.currentTimeMillis();
        secDiffTime = afterTime - beforeTime;
        log.info("JDBC save foreach 시간차이(ms): " + secDiffTime);
    }
}