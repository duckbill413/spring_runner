package com.example.runner.domain.s3file.service;

import com.example.runner.domain.s3file.dao.S3FileRepository;
import com.example.runner.domain.s3file.dto.response.S3FileRes;
import com.example.runner.domain.s3file.entity.S3File;
import com.example.runner.global.common.code.ErrorCode;
import com.example.runner.global.exception.BaseExceptionHandler;
import com.example.runner.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class S3FileServiceImpl implements S3FileService {
    private final S3Util s3Util;
    private final S3FileRepository s3FileRepository;

    @Override
    public S3FileRes upload(String path, MultipartFile file) throws IOException {
        return S3File.toDto(s3Util.upload(path, file));
    }

    @Override
    public void remove(UUID id) {
        s3Util.remove(id);
    }

    @Override
    public S3File findById(UUID id) {
        return s3FileRepository.findById(id).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_S3FILE));
    }

    @Override
    public Object[] getObject(UUID id) throws IOException {
        var s3File = s3FileRepository.findById(id).orElseThrow(() ->
                new BaseExceptionHandler(ErrorCode.NOT_FOUND_S3FILE));
        return new Object[]{s3Util.getObject(s3File.getS3DataUrl()),
                s3File.getOriginFileName()};
    }
}