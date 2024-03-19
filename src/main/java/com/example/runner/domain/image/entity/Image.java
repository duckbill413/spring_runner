package com.example.runner.domain.image.entity;

import com.example.runner.domain.BaseEntity;
import com.example.runner.domain.image.dto.response.ImageDetailRes;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;
    private String name;
    private String imageUrl;
    public static ImageDetailRes getDto(Image image) {
        return new ImageDetailRes(
                image.getName(),
                image.getImageUrl()
        );
    }
}
