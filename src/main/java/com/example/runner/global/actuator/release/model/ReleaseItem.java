package com.example.runner.global.actuator.release.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseItem {

    private String itemId;
    private String itemDescription;
}