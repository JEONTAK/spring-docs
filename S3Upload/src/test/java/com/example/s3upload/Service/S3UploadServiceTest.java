package com.example.s3upload.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class S3UploadServiceTest {

    @Autowired
    private S3UploadService s3UploadService;

    @Autowired
    private S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private ByteArrayInputStream testInputStream;
    private MockMultipartFile testMultipartFile;
    private final String testFileName = "test-file.txt";
    private byte[] testContent;


    @Test
    public void testUploadPerformanceComparisonWith1MB() throws IOException {
        // 테스트용 더미 파일 생성
        int testWith1MB = 1024 * 1024;
        testContent = new byte[testWith1MB];
        Arrays.fill(testContent, (byte) 0);
        testInputStream = new ByteArrayInputStream(testContent);
        testMultipartFile = new MockMultipartFile(
                "file",
                testFileName,
                "text/plain",
                testContent
        );

        // 성능 비교 테스트
        long startTime, endTime;
        double streamTime, multipartTime;

        // 메모리 사용량 비교 테스트
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore, memoryAfter;

        // Stream 방식
        startTime = System.currentTimeMillis();
        runtime.gc(); // 가비지 컬렉션 실행
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        s3UploadService.uploadViaStream(
                new ByteArrayInputStream(testContent),
                testFileName + "-stream"
        );
        endTime = System.currentTimeMillis();
        streamTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long streamMemoryUsed = memoryAfter - memoryBefore;

        // Multipart 방식
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        s3UploadService.uploadViaMultipart(testMultipartFile);
        endTime = System.currentTimeMillis();
        multipartTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long multipartMemoryUsed = memoryAfter - memoryBefore;

        // PreSigned URL 방식 (직접 업로드 시뮬레이션)
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        String presignedUrl = s3UploadService.generatePreSignedUrl(testFileName + "-presigned");
        startTime = System.currentTimeMillis();
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(testFileName + "-presigned").build(),
                RequestBody.fromBytes(testContent)
        );
        endTime = System.currentTimeMillis();
        double presignedTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long presignedMemoryUsed = memoryAfter - memoryBefore;

        // 결과 출력
        System.out.println("테스트 파일 크기: " + testContent.length / (1024 * 1024) + "MB");
        System.out.println("Stream Upload 방식 소모 시간: " + streamTime + " seconds");
        System.out.println("Stream Memory 사용량: " + streamMemoryUsed / 1024 + " KB");
        System.out.println("Multipart Upload 방식 소모 시간: " + multipartTime + " seconds");
        System.out.println("Multipart Memory 사용량: " + multipartMemoryUsed / 1024 + " KB");
        System.out.println("PreSigned URL Upload 방식 소모 시간: " + presignedTime + " seconds");
        System.out.println("PreSigned URL Memory 사용량: " + presignedMemoryUsed / 1024 + " KB");
    }

    @Test
    public void testUploadPerformanceComparisonWith5MB() throws IOException {
        // 테스트용 더미 파일 생성
        int testWith5MB = 5 * 1024 * 1024;
        testContent = new byte[testWith5MB];
        Arrays.fill(testContent, (byte) 0);
        testInputStream = new ByteArrayInputStream(testContent);
        testMultipartFile = new MockMultipartFile(
                "file",
                testFileName,
                "text/plain",
                testContent
        );

        // 성능 비교 테스트
        long startTime, endTime;
        double streamTime, multipartTime;

        // 메모리 사용량 비교 테스트
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore, memoryAfter;

        // Stream 방식
        startTime = System.currentTimeMillis();
        runtime.gc(); // 가비지 컬렉션 실행
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        s3UploadService.uploadViaStream(
                new ByteArrayInputStream(testContent),
                testFileName + "-stream"
        );
        endTime = System.currentTimeMillis();
        streamTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long streamMemoryUsed = memoryAfter - memoryBefore;

        // Multipart 방식
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        s3UploadService.uploadViaMultipart(testMultipartFile);
        endTime = System.currentTimeMillis();
        multipartTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long multipartMemoryUsed = memoryAfter - memoryBefore;

        // PreSigned URL 방식 (직접 업로드 시뮬레이션)
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        String presignedUrl = s3UploadService.generatePreSignedUrl(testFileName + "-presigned");
        startTime = System.currentTimeMillis();
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(testFileName + "-presigned").build(),
                RequestBody.fromBytes(testContent)
        );
        endTime = System.currentTimeMillis();
        double presignedTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long presignedMemoryUsed = memoryAfter - memoryBefore;

        // 결과 출력
        System.out.println("테스트 파일 크기: " + testContent.length / (1024 * 1024) + "MB");
        System.out.println("Stream Upload 방식 소모 시간: " + streamTime + " seconds");
        System.out.println("Stream Memory 사용량: " + streamMemoryUsed / 1024 + " KB");
        System.out.println("Multipart Upload 방식 소모 시간: " + multipartTime + " seconds");
        System.out.println("Multipart Memory 사용량: " + multipartMemoryUsed / 1024 + " KB");
        System.out.println("PreSigned URL Upload 방식 소모 시간: " + presignedTime + " seconds");
        System.out.println("PreSigned URL Memory 사용량: " + presignedMemoryUsed / 1024 + " KB");
    }

    @Test
    public void testUploadPerformanceComparisonWith100MB() throws IOException {
        // 테스트용 더미 파일 생성
        int testWith100MB = 100 * 1024 * 1024;
        testContent = new byte[testWith100MB];
        Arrays.fill(testContent, (byte) 0);
        testInputStream = new ByteArrayInputStream(testContent);
        testMultipartFile = new MockMultipartFile(
                "file",
                testFileName,
                "text/plain",
                testContent
        );

        // 성능 비교 테스트
        long startTime, endTime;
        double streamTime, multipartTime;

        // 메모리 사용량 비교 테스트
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore, memoryAfter;

        // Stream 방식
        startTime = System.currentTimeMillis();
        runtime.gc(); // 가비지 컬렉션 실행
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        s3UploadService.uploadViaStream(
                new ByteArrayInputStream(testContent),
                testFileName + "-stream"
        );
        endTime = System.currentTimeMillis();
        streamTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long streamMemoryUsed = memoryAfter - memoryBefore;

        // Multipart 방식
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        s3UploadService.uploadViaMultipart(testMultipartFile);
        endTime = System.currentTimeMillis();
        multipartTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long multipartMemoryUsed = memoryAfter - memoryBefore;

        // PreSigned URL 방식 (직접 업로드 시뮬레이션)
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        String presignedUrl = s3UploadService.generatePreSignedUrl(testFileName + "-presigned");
        startTime = System.currentTimeMillis();
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(testFileName + "-presigned").build(),
                RequestBody.fromBytes(testContent)
        );
        endTime = System.currentTimeMillis();
        double presignedTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long presignedMemoryUsed = memoryAfter - memoryBefore;

        // 결과 출력
        System.out.println("테스트 파일 크기: " + testContent.length / (1024 * 1024) + "MB");
        System.out.println("Stream Upload 방식 소모 시간: " + streamTime + " seconds");
        System.out.println("Stream Memory 사용량: " + streamMemoryUsed / 1024 + " KB");
        System.out.println("Multipart Upload 방식 소모 시간: " + multipartTime + " seconds");
        System.out.println("Multipart Memory 사용량: " + multipartMemoryUsed / 1024 + " KB");
        System.out.println("PreSigned URL Upload 방식 소모 시간: " + presignedTime + " seconds");
        System.out.println("PreSigned URL Memory 사용량: " + presignedMemoryUsed / 1024 + " KB");
    }

    @Test
    public void testUploadPerformanceComparisonWith200MB() throws IOException {
        // 테스트용 더미 파일 생성
        int testWith200MB = 200 * 1024 * 1024;
        testContent = new byte[testWith200MB];
        Arrays.fill(testContent, (byte) 0);
        testInputStream = new ByteArrayInputStream(testContent);
        testMultipartFile = new MockMultipartFile(
                "file",
                testFileName,
                "text/plain",
                testContent
        );

        // 성능 비교 테스트
        long startTime, endTime;
        double streamTime, multipartTime;

        // 메모리 사용량 비교 테스트
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore, memoryAfter;

        // Stream 방식
        startTime = System.currentTimeMillis();
        runtime.gc(); // 가비지 컬렉션 실행
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        s3UploadService.uploadViaStream(
                new ByteArrayInputStream(testContent),
                testFileName + "-stream"
        );
        endTime = System.currentTimeMillis();
        streamTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long streamMemoryUsed = memoryAfter - memoryBefore;

        // Multipart 방식
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        s3UploadService.uploadViaMultipart(testMultipartFile);
        endTime = System.currentTimeMillis();
        multipartTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long multipartMemoryUsed = memoryAfter - memoryBefore;

        // PreSigned URL 방식 (직접 업로드 시뮬레이션)
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        String presignedUrl = s3UploadService.generatePreSignedUrl(testFileName + "-presigned");
        startTime = System.currentTimeMillis();
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(testFileName + "-presigned").build(),
                RequestBody.fromBytes(testContent)
        );
        endTime = System.currentTimeMillis();
        double presignedTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long presignedMemoryUsed = memoryAfter - memoryBefore;

        // 결과 출력
        System.out.println("테스트 파일 크기: " + testContent.length / (1024 * 1024) + "MB");
        System.out.println("Stream Upload 방식 소모 시간: " + streamTime + " seconds");
        System.out.println("Stream Memory 사용량: " + streamMemoryUsed / 1024 + " KB");
        System.out.println("Multipart Upload 방식 소모 시간: " + multipartTime + " seconds");
        System.out.println("Multipart Memory 사용량: " + multipartMemoryUsed / 1024 + " KB");
        System.out.println("PreSigned URL Upload 방식 소모 시간: " + presignedTime + " seconds");
        System.out.println("PreSigned URL Memory 사용량: " + presignedMemoryUsed / 1024 + " KB");
    }

    @Test
    public void testUploadPerformanceComparisonWith300MB() throws IOException {
        // 테스트용 더미 파일 생성
        int testWith300MB = 300 * 1024 * 1024;
        testContent = new byte[testWith300MB];
        Arrays.fill(testContent, (byte) 0);
        testInputStream = new ByteArrayInputStream(testContent);
        testMultipartFile = new MockMultipartFile(
                "file",
                testFileName,
                "text/plain",
                testContent
        );

        // 성능 비교 테스트
        long startTime, endTime;
        double streamTime, multipartTime;

        // 메모리 사용량 비교 테스트
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore, memoryAfter;

        // Stream 방식
        startTime = System.currentTimeMillis();
        runtime.gc(); // 가비지 컬렉션 실행
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        s3UploadService.uploadViaStream(
                new ByteArrayInputStream(testContent),
                testFileName + "-stream"
        );
        endTime = System.currentTimeMillis();
        streamTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long streamMemoryUsed = memoryAfter - memoryBefore;

        // Multipart 방식
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.currentTimeMillis();
        s3UploadService.uploadViaMultipart(testMultipartFile);
        endTime = System.currentTimeMillis();
        multipartTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long multipartMemoryUsed = memoryAfter - memoryBefore;

        // PreSigned URL 방식 (직접 업로드 시뮬레이션)
        runtime.gc();
        memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        String presignedUrl = s3UploadService.generatePreSignedUrl(testFileName + "-presigned");
        startTime = System.currentTimeMillis();
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(testFileName + "-presigned").build(),
                RequestBody.fromBytes(testContent)
        );
        endTime = System.currentTimeMillis();
        double presignedTime = (endTime - startTime) / 1000.0;
        memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long presignedMemoryUsed = memoryAfter - memoryBefore;

        // 결과 출력
        System.out.println("테스트 파일 크기: " + testContent.length / (1024 * 1024) + "MB");
        System.out.println("Stream Upload 방식 소모 시간: " + streamTime + " seconds");
        System.out.println("Stream Memory 사용량: " + streamMemoryUsed / 1024 + " KB");
        System.out.println("Multipart Upload 방식 소모 시간: " + multipartTime + " seconds");
        System.out.println("Multipart Memory 사용량: " + multipartMemoryUsed / 1024 + " KB");
        System.out.println("PreSigned URL Upload 방식 소모 시간: " + presignedTime + " seconds");
        System.out.println("PreSigned URL Memory 사용량: " + presignedMemoryUsed / 1024 + " KB");
    }
}