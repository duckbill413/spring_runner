package com.example.learner.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.learner.domain.s3file.dao.S3FileRepository;
import com.example.learner.domain.s3file.entity.S3File;
import com.example.learner.global.common.code.ErrorCode;
import com.example.learner.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class S3Util {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final S3FileRepository s3FileRepository;
    // 파일 업로드
    @Transactional
    public S3File upload(String path, MultipartFile file) throws IOException {

        String originFileName = file.getOriginalFilename(); // 원본 파일명
        String folderKey = path + "/"; // 폴더 주소
        UUID randomId = UUID.randomUUID();
        String fileName = randomId + "_" + originFileName; // 변환된 파일명

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        // 폴더가 없는 경우 생성
        if (!amazonS3.doesObjectExist(bucket, folderKey)) {
            amazonS3.putObject(bucket, folderKey, "");
        }
        // 파일 업로드
        String objectKey = folderKey + fileName;
        amazonS3.putObject(new PutObjectRequest(bucket, objectKey, file.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String fileUrl = amazonS3.getUrl(bucket, objectKey).toString();


        var s3File = S3File.builder()
                .id(randomId)
                .originFileName(originFileName)
                .fileDir(folderKey)
                .fileName(fileName)
                .s3DataUrl(fileUrl)
                .build();
        return s3FileRepository.save(s3File);
    }

    @Transactional
    public void remove(UUID id) {
        var s3File = s3FileRepository.findById(id)
                .orElseThrow(() -> new BaseExceptionHandler(ErrorCode.NOT_FOUND_S3FILE));

        // S3 내 파일 삭제
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, s3File.getS3DataUrl());
        amazonS3.deleteObject(deleteObjectRequest);

        // db의 s3 파일 데이터 상태 변경(삭제)
        s3FileRepository.deleteById(id);
    }

    public byte[] getObject(String s3FileUrl) throws IOException {
        // bucket 와 fileDir 을 사용해서 S3 에 있는 객체 - object - 를 가져온다.
        // TODO: fileDataUrl 가 맞는지 체크
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket, s3FileUrl));

        // object 를 S3ObjectInputStream 형태로 변환한다.
        S3ObjectInputStream objectInputStream = object.getObjectContent();

        // 이후 다시 byte 배열 형태로 변환한다.
        // 아마도 파일 전송을 위해서는 다시 byte[] 즉, binary 로 변환해서 전달해야햐기 때문

        return IOUtils.toByteArray(objectInputStream);
    }
}