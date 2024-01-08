/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.learner.domain.menu.dao;

import com.example.learner.domain.menu.domain.Menu;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j2
@Repository
@RequiredArgsConstructor
public class MenuJDBCRepositoryImpl implements MenuJDBCRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<Menu> MENU_ROW_MAPPER = (rs, rowNum) -> Menu.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .price(rs.getLong("price"))
            .stock(rs.getLong("stock"))
            .build();

    @Override
    public Optional<Menu> findById(Long id) {
        var sql = """
                SELECT
                id,
                name,
                price,
                stock
                FROM Menu
                WHERE id = :id
                """;

        var params = new MapSqlParameterSource()
                .addValue("id", id);

        var menu = namedParameterJdbcTemplate.queryForObject(sql, params, MENU_ROW_MAPPER);
        return Optional.ofNullable(menu);
    }

    @Override
    public int saveAll(List<Menu> menus) {
        // JDBC의 bulk insert를 이용해 보겠다.

        var sql = """
                INSERT INTO Menu (name, price, stock)
                VALUES (:name, :price, :stock)
                """;

        SqlParameterSource[] params = menus
                .stream().map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        var result = namedParameterJdbcTemplate.batchUpdate(sql, params);
        return Arrays.stream(result).sum();
    }

    @Override
    public Menu save(Menu menu) {
        // JPA의 save는 insert 뿐만 아니라 update의 기능도 역시 수행한다.
        // Entity의 id 값이 있으면 update 없다면 insert가 발생한다.

        // id 값이 없는 경우 insert
        if (menu.getId() == null) {
            return insert(menu);
        }
        return update(menu);
    }

    private Menu update(Menu menu) {
        String sql = """
                UPDATE Menu Set
                name = :name,
                price = :price,
                stock = :stock
                WHERE id=:id
                """;

        var params = new BeanPropertySqlParameterSource(menu);
        var updatedCount = namedParameterJdbcTemplate.update(sql, params);

        if (updatedCount == 0) throw new BaseExceptionHandler(ErrorCode.FAILED_TO_UPDATE_MENU);
        return menu;
    }

    private Menu insert(Menu menu) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("Menu")
                .usingGeneratedKeyColumns("id"); // @GeneratedValue

        SqlParameterSource params = new BeanPropertySqlParameterSource(menu);
        var id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Menu.builder()
                .id(id)
                .name(menu.getName())
                .price(menu.getPrice())
                .stock(menu.getStock())
                .build();
    }
}
