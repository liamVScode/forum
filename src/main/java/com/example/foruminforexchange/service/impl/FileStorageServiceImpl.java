package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.service.FileStorageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${file.storage.location}") String fileStorageLocation) {
        this.fileStorageLocation = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        // Kiểm tra đuôi file
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.matches("jpg|jpeg|png|gif")) {
            throw new RuntimeException("Only image files are allowed!");
        }

        // Kiểm tra kích thước file
        long fileSize = file.getSize();
        if (fileSize > 5 * 1024 * 1024) { // 5 MB
            throw new RuntimeException("File size exceeds the allowable limit (5MB)!");
        }

        // Tạo tên file duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // Copy file vào đường dẫn lưu trữ
        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            file.transferTo(targetLocation);
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + uniqueFileName + ". Please try again!", ex);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            Path file = fileStorageLocation.resolve(filePath).normalize();
            Files.deleteIfExists(file);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete the file: " + filePath, ex);
        }
    }
}
