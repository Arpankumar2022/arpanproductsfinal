package com.arpanbags.products.arpanbagsproducts.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping("/images")
public class ImageControllerV2 {

    private static final String IMAGE_UPLOAD_DIR = "/opt/arpanbags/uploads/images/";
    private static final Logger logger = LoggerFactory.getLogger(ImageControllerV2.class);

    public ImageControllerV2() {
        File dir = new File(IMAGE_UPLOAD_DIR);
        if (!dir.exists()) {
            boolean mkdir = dir.mkdirs();
            logger.info("Folder directory is created {} ", mkdir);
        }
    }

    // Upload Image
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileUrl = null;
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            logger.info("IMAGE_UPLOAD_DIR: {} ", IMAGE_UPLOAD_DIR);
            Path targetPath = Path.of(IMAGE_UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String baseUrl = getBaseUrl(request);
            fileUrl = baseUrl + "/images/files/" + fileName;
            logger.info("fileUrl: {} ", fileUrl);

            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            logger.error("Error uploading image for fileUrl: {}", fileUrl, e);
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }

    // List All Images (Absolute URLs)
    @GetMapping("/list")
    public ResponseEntity<List<String>> listImages(HttpServletRequest request) {
        String baseUrl = getBaseUrl(request);
        logger.info("listImages:: baseUrl: {} ", baseUrl);
        logger.info("listImages:: IMAGE_UPLOAD_DIR: {} ", IMAGE_UPLOAD_DIR);

        try (Stream<Path> paths = Files.list(Path.of(IMAGE_UPLOAD_DIR))) {
            List<String> imageUrls = paths
                    .filter(Files::isRegularFile)
                    .map(path -> baseUrl + "/images/files/" + path.getFileName().toString())
                    .toList();

            return ResponseEntity.ok(imageUrls);

        } catch (IOException e) {
            logger.error("Error in fetching listImages", e);
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }


    // Helper to get server base URL
    public static String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }


    @PostMapping("/uploadFiles")
    public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        String baseUrl = getBaseUrl(request);
        List<String> urls = Arrays.stream(files)
                .map(file -> {
                    try {
                        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        Path path = Path.of(IMAGE_UPLOAD_DIR, fileName);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        return baseUrl + "/images/files/" + fileName;
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload: " + file.getOriginalFilename(), e);
                    }
                })
                .toList();

        return ResponseEntity.ok(urls);
    }
}