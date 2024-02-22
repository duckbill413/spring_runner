/*
 * author : duckbill413
 * created date : 2024-01-07
 * updated date : 2024-01-07
 * description :
 */

package com.example.learner.domain.menu.api;

import com.example.learner.domain.menu.application.MenuService;
import com.example.learner.domain.menu.dto.request.InsertMenuReq;
import com.example.learner.domain.menu.dto.request.InsertMenusReq;
import com.example.learner.domain.menu.dto.response.MenuDetailRes;
import com.example.learner.global.common.BaseResponse;
import com.example.learner.global.common.ErrorResponse;
import com.example.learner.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "메뉴", description = "Menu 관련 API")
@Log4j2
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @Operation(
            summary = "메뉴 여러개 추가",
            responses = {
                    @ApiResponse(responseCode = "200", description = "메뉴 추가 성공", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "메뉴 추가 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("/dozen")
    public ResponseEntity<BaseResponse<String>> insertMenus(
            @RequestBody InsertMenusReq menusDto
    ) {
        var result = menuService.insertMenus(menusDto);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                String.format("메뉴 %s개 추가 성공", result)
        );
    }

    @PostMapping("")
    public ResponseEntity<BaseResponse<MenuDetailRes>> insertMenu(@RequestBody InsertMenuReq menuDto) {
        var result = menuService.insertMenu(menuDto);
        return BaseResponse.success(
                SuccessCode.INSERT_SUCCESS,
                result
        );
    }

    @Operation(
            summary = "메뉴 조회",
            description = "메뉴 Id를 이용한 메뉴 정보 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "메뉴 조회 성공", content = @Content(schema = @Schema(implementation = MenuDetailRes.class))),
                    @ApiResponse(responseCode = "404", description = "메뉴 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<MenuDetailRes>> findMenu(
            @PathVariable Long id
    ) {
        var menuDto = menuService.findMenu(id);
        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                menuDto
        );
    }
}
