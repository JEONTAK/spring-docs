package com.example.s3upload.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;
    @Getter
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 1. Stream 방식
    public Double uploadViaStream(HttpServletRequest request, String fileName) throws IOException {
        double startTime = System.currentTimeMillis();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(request.getContentType())
                .contentLength(request.getContentLengthLong())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(request.getInputStream(), request.getInputStream().available()));
        double endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000;
    }

    // 2. Multipartfile 방식
    public Double uploadViaMultipart(MultipartFile file) throws IOException {
        double startTime = System.currentTimeMillis();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        /*byte[] fileBytes = file.getBytes();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                new ByteArrayInputStream(fileBytes), fileBytes.length));*/

        double endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000;
    }

    // 3. PreSigned URL 생성
    public Double generatePreSignedUrl(String fileName) {
        double startTime = System.currentTimeMillis();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();
        s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileName));
        double endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000;
    }

}