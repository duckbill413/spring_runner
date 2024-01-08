package com.example.learner.domain.menu.application;

import com.example.learner.domain.menu.dao.MenuJDBCRepository;
import com.example.learner.domain.menu.dao.MenuRepository;
import com.example.learner.domain.menu.domain.Menu;
import com.example.learner.domain.menu.dto.request.InsertMenuDto;
import com.example.learner.domain.menu.dto.request.InsertMenus;
import com.example.learner.domain.menu.dto.response.MenuInfo;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Menu Service 구현체
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuJDBCRepository menuJDBCRepository;
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;

    /**
     * 새로운 메뉴 추가
     *
     * @param menusDto 메뉴 dto List
     * @return 추가된 메뉴의 개수
     */
    @Override
    public int insertMenus(InsertMenus menusDto) {
        var menus = menusDto.menus().stream().map(menuDto ->
                modelMapper.map(menuDto, Menu.class)).toList();
        return menuJDBCRepository.saveAll(menus);
    }

    @Override
    public MenuInfo insertMenu(InsertMenuDto menuDto) {
        var menu = menuJDBCRepository.save(Menu.builder()
                .name(menuDto.name())
                .price(menuDto.price())
                .build());

        return new MenuInfo(menu.getId(), menu.getName(), menu.getPrice(), menu.getStock());
    }

    /**
     * 메뉴 검색
     *
     * @param menuId 메뉴의 ID
     * @return MenuInfo 메뉴 정보
     */
    @Override
    public MenuInfo findMenu(Long menuId) {
        var menu = menuJDBCRepository.findById(menuId).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_MENU_EXCEPTION));
        return new MenuInfo(menu.getId(), menu.getName(), menu.getPrice(), menu.getStock());
    }
}
