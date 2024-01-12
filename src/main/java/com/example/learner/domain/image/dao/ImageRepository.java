package com.example.learner.domain.image.dao;

import com.example.learner.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
