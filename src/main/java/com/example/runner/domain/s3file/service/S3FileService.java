package com.example.runner.domain.s3file.service;

import com.example.runner.domain.s3file.dto.response.S3FileRes;
import com.example.runner.domain.s3file.entity.S3File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface S3FileService {
    S3FileRes upload(String path, MultipartFile file) throws IOException;
    void remove(UUID id);
    S3File findById(UUID id);
    Object[] getObject(UUID id) throws IOException;
}