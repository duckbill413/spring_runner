package com.example.learner.domain.s3file.controller;

import com.example.learner.domain.s3file.dto.response.S3FileRes;
import com.example.learner.domain.s3file.service.S3FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "파일", description = "파일 업로드/다운로드")
public class S3FileController {
    private final S3FileService s3FileService;

    // 프론트에서 ajax 를 통해 /upload 로 MultipartFile 형태로 파일과 roomId 를 전달받는다.
    // 전달받은 file 를 uploadFile 메서드를 통해 업로드한다.
    @PostMapping("/{path}")
    public S3FileRes uploadFile(@PathVariable("path") String path,
                                @RequestParam("file") MultipartFile file) throws IOException {
        // fileReq 객체 리턴
        return s3FileService.upload(path, file);
    }

    // get 으로 요청이 오면 아래 download 메서드를 실행한다.
    // fileName 과 파라미터로 넘어온 fileDir 을 getObject 메서드에 매개변수로 넣는다.
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable(name = "fileId") UUID fileId) throws IOException {
        // 변환된 byte, httpHeader 와 HttpStatus 가 포함된 ResponseEntity 객체를 return 한다.
        var byteFile = s3FileService.getObject(fileId);

        // 여기는 httpHeader 에 파일 다운로드 요청을 하기 위한내용
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 지정된 fileName 으로 파일이 다운로드 된다.
        httpHeaders.setContentDispositionFormData("attachment", (String) byteFile[1]);

        // 최종적으로 ResponseEntity 객체를 리턴하는데
        // --> ResponseEntity 란?
        // ResponseEntity 는 사용자의 httpRequest 에 대한 응답 테이터를 포함하는 클래스이다.
        // 단순히 body 에 데이터를 포함하는 것이 아니라, header 와 httpStatus 까지 넣어 줄 수 있다.
        // 이를 통해서 header 에 따라서 다른 동작을 가능하게 할 수 있다 => 파일 다운로드!!

        // 나는 object가 변환된 byte 데이터, httpHeader 와 HttpStatus 가 포함된다.
        return new ResponseEntity<>((byte[]) byteFile[0], httpHeaders, HttpStatus.OK);
    }
}