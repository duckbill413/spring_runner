package com.example.learner.domain.menu.application;

import com.example.learner.domain.menu.dto.request.InsertMenuDto;
import com.example.learner.domain.menu.dto.request.InsertMenus;
import com.example.learner.domain.menu.dto.response.MenuInfo;

public interface MenuService {
    int insertMenus(InsertMenus menusDto);
    MenuInfo insertMenu(InsertMenuDto menuDto);
    MenuInfo findMenu(Long menuId);
}
