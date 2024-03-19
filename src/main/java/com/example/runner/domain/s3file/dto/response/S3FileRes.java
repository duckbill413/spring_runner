package com.example.runner.domain.s3file.dto.response;

import java.util.UUID;

public record S3FileRes(
        UUID id,
        String originFileName,
        String fileName,
        String s3DataUrl,
        String fileDir
) {
}
