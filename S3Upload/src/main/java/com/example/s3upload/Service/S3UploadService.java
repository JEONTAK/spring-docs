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

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                file.getInputStream(), file.getSize()));
        double endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000;
        /*CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .build();
        CreateMultipartUploadResponse createResponse = s3Client.createMultipartUpload(createRequest);

        List<CompletedPart> parts = new ArrayList<>();
        int partNumber = 1;
        long chunkSize = 5 * 1024 * 1024; // 5MB chunks
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[(int) chunkSize];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);
                UploadPartRequest partRequest = UploadPartRequest.builder()
                        .bucket(bucketName)
                        .key(file.getOriginalFilename())
                        .uploadId(createResponse.uploadId())
                        .partNumber(partNumber)
                        .build();
                UploadPartResponse partResponse = s3Client.uploadPart(partRequest, RequestBody.fromByteBuffer(byteBuffer));
                parts.add(CompletedPart.builder()
                        .partNumber(partNumber)
                        .eTag(partResponse.eTag())
                        .build());
                partNumber++;
            }
        }

        s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .uploadId(createResponse.uploadId())
                .multipartUpload(CompletedMultipartUpload.builder().parts(parts).build())
                .build());*/
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

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }
}