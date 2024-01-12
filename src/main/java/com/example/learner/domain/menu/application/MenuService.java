package com.example.learner.domain.menu.application;

import com.example.learner.domain.menu.dto.request.InsertMenuReq;
import com.example.learner.domain.menu.dto.request.InsertMenusReq;
import com.example.learner.domain.menu.dto.response.MenuDetailRes;

public interface MenuService {
    int insertMenus(InsertMenusReq menusDto);
    MenuDetailRes insertMenu(InsertMenuReq menuDto);
    MenuDetailRes findMenu(Long menuId);
}
