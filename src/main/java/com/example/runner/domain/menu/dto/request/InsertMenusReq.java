package com.example.runner.domain.menu.dto.request;

import java.util.List;

public record InsertMenusReq(
        List<InsertMenuReq> menus
) {
}
