package com.arpanbags.products.arpanbagsproducts.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final String IMAGE_UPLOAD_DIR = "/opt/arpanbags/uploads/images/";
    private static final String RESOURCE_HANDLER_PREFIX = "/images/files/";
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController() {
        File dir = new File(IMAGE_UPLOAD_DIR);
        if (!dir.exists()) {
            boolean mkdir = dir.mkdirs();
            logger.info("Folder directory is created {} ", mkdir);
        }
    }

    // Upload single image
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = Path.of(IMAGE_UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = buildFileUrl(request, fileName);
            logger.info("Uploaded image URL: {}", fileUrl);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            logger.error("Error uploading image", e);
            return ResponseEntity.status(500).body("Error uploading image: " + e.getMessage());
        }
    }

    // List all images
    @GetMapping("/list")
    public ResponseEntity<List<String>> listImages(HttpServletRequest request) {
        try (Stream<Path> paths = Files.list(Path.of(IMAGE_UPLOAD_DIR))) {
            List<String> imageUrls = paths
                    .filter(Files::isRegularFile)
                    .map(path -> buildFileUrl(request, path.getFileName().toString()))
                    .toList();

            return ResponseEntity.ok(imageUrls);
        } catch (IOException e) {
            logger.error("Error in fetching listImages", e);
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    // Upload multiple images
    @PostMapping("/uploadFiles")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Path.of(IMAGE_UPLOAD_DIR, fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                urls.add(buildFileUrl(request, fileName));
            } catch (IOException e) {
                logger.error("Failed to upload: {}", file.getOriginalFilename(), e);
            }
        }
        return ResponseEntity.ok(urls);
    }

    // Helper: Build public URL for a given filename
    public static String buildFileUrl(HttpServletRequest request, String fileName) {
        return getBaseUrl(request) + RESOURCE_HANDLER_PREFIX + fileName;
    }

    // Helper: Base URL
    public static String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/arpanproducts";
    }
}
