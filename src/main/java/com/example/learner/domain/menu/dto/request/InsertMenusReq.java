package com.example.learner.domain.menu.dto.request;

import java.util.List;

public record InsertMenusReq(
        List<InsertMenuReq> menus
) {
}
