package com.example.learner.domain.menu.application;

import com.example.learner.domain.menu.dao.MenuJdbcRepository;
import com.example.learner.domain.menu.dao.MenuRepository;
import com.example.learner.domain.menu.entity.Menu;
import com.example.learner.domain.menu.dto.request.InsertMenuReq;
import com.example.learner.domain.menu.dto.request.InsertMenusReq;
import com.example.learner.domain.menu.dto.response.MenuDetailRes;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * Menu Service 구현체
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuJdbcRepository menuJdbcRepository;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    /**
     * 새로운 메뉴 추가
     *
     * @param menusDto 메뉴 dto List
     * @return 추가된 메뉴의 개수
     */
    @Override
    public int insertMenus(InsertMenusReq menusDto) {
        var menus = menusDto.menus().stream().map(menuDto ->
                modelMapper.map(menuDto, Menu.class)).toList();
        return menuJdbcRepository.saveAll(menus);
    }

    @Override
    public MenuDetailRes insertMenu(InsertMenuReq menuDto) {
        var menu = menuJdbcRepository.save(Menu.builder()
                .name(menuDto.name())
                .price(menuDto.price())
                .build());

        return new MenuDetailRes(menu.getId(), menu.getName(), menu.getPrice(), menu.getStock());
    }

    /**
     * 메뉴 검색
     *
     * @param menuId 메뉴의 ID
     * @return MenuInfo 메뉴 정보
     */
    @Override
    public MenuDetailRes findMenu(Long menuId) {
        var menu = menuJdbcRepository.findById(menuId).orElseThrow();
        return new MenuDetailRes(menu.getId(), menu.getName(), menu.getPrice(), menu.getStock());
    }
}
