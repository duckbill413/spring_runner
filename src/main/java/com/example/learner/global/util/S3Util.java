package com.example.learner.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.learner.domain.s3file.entity.S3File;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class S3Util {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    // 파일 업로드
    public S3File upload(UUID id, MultipartFile file) throws IOException {

        String originFileName = file.getOriginalFilename(); // 원본 파일명
        String folderKey = id.toString() + "/"; // 폴더 주소
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


        return S3File.builder()
                .id(randomId)
                .originFileName(originFileName)
                .fileName(fileName)
                .s3DataUrl(fileUrl)
                .fileDir(id.toString())
                .build();
    }

    // 파일 삭제
    public void remove(UUID id, String fileId) {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, id.toString() + "/" + fileId);
        amazonS3.deleteObject(deleteObjectRequest);
    }

    public void remove(UUID id) {
        String folderKey = id + "/";
        List<S3ObjectSummary> objectSummaries = amazonS3.listObjects(bucket, folderKey).getObjectSummaries();

        // 내부 오브젝트 삭제
        for (S3ObjectSummary summary : objectSummaries) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, summary.getKey()));
        }

        // 폴더 삭제
        amazonS3.deleteObject(bucket, folderKey);
    }

    public byte[] getObject(String fileDataUrl) throws IOException {
        // bucket 와 fileDir 을 사용해서 S3 에 있는 객체 - object - 를 가져온다.
        // TODO: fileDataUrl 가 맞는지 체크
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket, fileDataUrl));

        // object 를 S3ObjectInputStream 형태로 변환한다.
        S3ObjectInputStream objectInputStream = object.getObjectContent();

        // 이후 다시 byte 배열 형태로 변환한다.
        // 아마도 파일 전송을 위해서는 다시 byte[] 즉, binary 로 변환해서 전달해야햐기 때문

        return IOUtils.toByteArray(objectInputStream);
    }
}
