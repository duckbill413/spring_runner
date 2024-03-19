/*
 * author : duckbill413
 * created date : 2024-01-07
 * updated date : 2024-01-07
 * description :
 */

package com.example.runner.domain.menu.application;

import com.example.runner.domain.menu.dao.MenuRepository;
import com.example.runner.domain.menu.entity.Menu;
import com.example.runner.util.MenuFixtureFactory;
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
class MenuServiceImplTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("메뉴 정보 불러오기")
    public void findMenu() {
        // Given
        var parameters = MenuFixtureFactory.getMenuParams();
        var easyRandom = new EasyRandom(parameters);
        List<Menu> menus = IntStream.range(0, 100)
                .parallel()
                .mapToObj(value -> easyRandom.nextObject(Menu.class))
                .toList();

        var savedMenus = menuRepository.saveAll(menus);

        // When
        var menuId = savedMenus.get(0).getId();

        // Then
        log.info(menuService.findMenu(menuId));
    }

    @Test
    @DisplayName("JPA 메뉴 정보 저장 테스트")
    public void insertMenusTest() {
        // Given
        var parameters = MenuFixtureFactory.getMenuParams();
        var easyRandom = new EasyRandom(parameters);

        // saveAll insert test
        List<Menu> menus1 = IntStream.range(0, 30000)
                .parallel().mapToObj(value -> easyRandom.nextObject(Menu.class))
                .toList();

        // Test saveAll
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        var savedMenus = menuRepository.saveAll(menus1);
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = afterTime - beforeTime; //두 시간에 차 계산
        log.info("JPA saveAll 시간차이(ms): " + secDiffTime);
    }
}