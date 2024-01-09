package com.example.learner.domain.s3file.entity;

import com.example.learner.domain.s3file.dto.response.S3FileRes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class S3File {
    @Id
    @Column(name = "s3file_id")
    private UUID id;
    private String originFileName; // 파일 원본 이름
    private String fileName; // 변환된 파일명
    private String s3DataUrl; // 파일 링크
    private String fileDir; // S3 파일 경로

    @Builder
    public S3File(UUID id, String originFileName, String fileName, String s3DataUrl, String fileDir) {
        this.id = id;
        this.fileName = fileName;
        this.originFileName = originFileName;
        this.s3DataUrl = s3DataUrl;
        this.fileDir = fileDir;
    }

    public static S3FileRes toDto(S3File s3File) {
        return new S3FileRes(
                s3File.getId(),
                s3File.getOriginFileName(),
                s3File.getFileName(),
                s3File.getS3DataUrl(),
                s3File.getS3DataUrl()
        );
    }
}