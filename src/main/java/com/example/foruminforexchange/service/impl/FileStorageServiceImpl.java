package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl() {
        this.fileStorageLocation = Paths.get("src/main/resources/static/Image").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.matches("jpg|jpeg|png|gif")) {
            throw new RuntimeException("Only image files are allowed!");
        }

        // size
        long fileSize = file.getSize();
        if (fileSize > 5 * 1024 * 1024) { // 5 MB
            throw new RuntimeException("File size exceeds the allowable limit (5MB)!");
        }

        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // save
        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            file.transferTo(targetLocation);

            // Trả về URL của tệp vừa lưu
            return "https://localhost:5000/Image/" + uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + uniqueFileName + ". Please try again!", ex);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            if (filePath.startsWith("http")) {
                URI uri = new URI(filePath);
                filePath = Paths.get(uri.getPath()).getFileName().toString();
            }

            Path file = fileStorageLocation.resolve(filePath).normalize();
            Files.deleteIfExists(file);
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException("Failed to delete the file: " + filePath, ex);
        }
    }

}
