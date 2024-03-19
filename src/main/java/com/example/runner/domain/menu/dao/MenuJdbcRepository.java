/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.runner.domain.menu.dao;

import com.example.runner.domain.menu.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuJdbcRepository {
    int saveAll(List<Menu> menus);

    Menu save(Menu menu);

    Optional<Menu> findById(Long id);
    List<Menu> findAll();
}
