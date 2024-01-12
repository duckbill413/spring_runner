package com.example.learner.domain.s3file.dao;

import com.example.learner.domain.s3file.entity.S3File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface S3FileRepository extends JpaRepository<S3File, UUID> {
    void deleteByFileDir(String fileDir);
}
