package com.example.s3upload.Controller;

import com.example.s3upload.Service.S3UploadService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class S3UploadController {

    private final S3UploadService s3UploadService;

    @PostMapping("/stream")
    public ResponseEntity<String> uploadStream(HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(s3UploadService.uploadViaStream(request, request.getHeader("File-Name")) + "초");
    }

    @PostMapping("/multipart")
    public ResponseEntity<String> uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(s3UploadService.uploadViaMultipart(file) + "초");
    }

    @PostMapping("/generate-presigned-url")
    public ResponseEntity<String> generatePreSignedUrl(@RequestBody String filename) {
        return ResponseEntity.ok(s3UploadService.generatePreSignedUrl(filename) + "초");
    }
}
