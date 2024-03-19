package com.example.runner.domain.restaurant.dao;

import com.example.runner.domain.image.dao.ImageRepository;
import com.example.runner.domain.image.entity.Image;
import com.example.runner.domain.menu.dao.MenuRepository;
import com.example.runner.domain.menu.entity.Menu;
import com.example.runner.domain.restaurant.entity.Category;
import com.example.runner.domain.restaurant.entity.Restaurant;
import com.example.runner.util.MenuFixtureFactory;
import lombok.extern.log4j.Log4j2;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
class RestaurantRepositoryTest {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("eager loading 테스트")
    public void eagerLoadingTest() {
        var id = UUID.fromString("b95b95f5-6fcb-4519-8413-8cba9919742d");
        var restaurant = restaurantRepository.findById(id).orElse(null);
        assert restaurant != null;

        // MEMO: sql를 확인하였을때 image field는 eager이라 join이 발생하지만
        // MEMO: menus는 lazy loading이기 때문에 join을 통한 조회가 일어나지 않는다.
        log.info("레스토랑ID: " + restaurant.getId());
    }

    @Test
    @DisplayName("eager loading 테스트")
    @Transactional(readOnly = true)
    public void lazyLoadingTest() {
        var id = UUID.fromString("b95b95f5-6fcb-4519-8413-8cba9919742d");
        var restaurant = restaurantRepository.findById(id).orElse(null);
        assert restaurant != null;
        var menus = restaurant.getMenus();
        log.info("레스토랑ID: " + restaurant.getId()); // break point
        log.info("메뉴 개수: " + menus.size());
    }

    /**
     * Cascade Remove 테스트
     * JPA N+1 문제도 확인 가능
     */
    @Test
    @DisplayName("cascade remove test")
    public void cascadeRemoveTest() {
        var id = UUID.fromString("dbded1a7-b46e-46a3-a4f3-0f38ccbaee70");
        var restaurant = restaurantRepository.findById(id).orElse(null);
        assert restaurant != null;

        var image = restaurant.getImage();
        log.info("이미지 ID: " + image.getId());
        restaurant.setImage(null);
        restaurantRepository.save(restaurant);

        var findImage = imageRepository.findById(image.getId());
        if (findImage.isPresent()) {
            log.info("이미지 존재");
        } else {
            log.info("이미지 삭제됨");
        }
        Assertions.assertNotNull(findImage.orElse(null));
    }

    @Test
    @DisplayName("orphanRemoval test")
    @Transactional
    public void orphanRemovalTest() {
        var id = UUID.fromString("dbded1a7-b46e-46a3-a4f3-0f38ccbaee70");
        var restaurant = restaurantRepository.findById(id).orElse(null);
        assert restaurant != null;
        var restaurantMenuCount = (long) restaurant.getMenus().size();


        // 전체 등록된 메뉴 개수
        long menuCount = menuRepository.count();

        // 매장 삭제
        restaurantRepository.deleteById(id);

        // 매장 삭제 이후 전체 매뉴 개수
        long menuCountAfter = menuRepository.count();

        log.info("전체 등록 메뉴 개수: " + menuCount);
        log.info("매장의 메뉴 개수: " + restaurantMenuCount);
        log.info("삭제 이후 메뉴 개수: " + menuCountAfter);

        Assertions.assertEquals(menuCount - restaurantMenuCount, menuCountAfter);
    }


    @Test
    @DisplayName("레스토랑 저장 테스트")
    @Transactional
    @Rollback(value = false)
    public void saveRestaurant() {
        Restaurant restaurant = Restaurant.builder()
                .name("홍콩반점")
                .phone("02-1111-2222")
                .category(Category.CHINESE)
                .build();

        var parameters = MenuFixtureFactory.getMenuParams();
        var easyRandom = new EasyRandom(parameters);
        // saveAll insert test
        List<Menu> menus = IntStream.range(0, 20)
                .parallel().mapToObj(value -> easyRandom.nextObject(Menu.class))
                .peek(menu -> menu.setRestaurant(restaurant))
                .toList();
        restaurant.setMenus(menus); // 없어도 가능 @Builder.Default

        Image img = Image.builder()
                .name("홍콩Img")
                .imageUrl("홍콩Img_Url.jpg")
                .build();
        restaurant.setImage(img);

        var result = restaurantRepository.save(restaurant);
        log.info(result.getId());
    }

    @DisplayName("Fetch Join Test")
    @Test
    public void fetchJoinTest() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        log.info("레스토랑의 개수: " + restaurants.size());
    }
}