package com.litethinking.enterprise.application.service;

import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.infrastructure.persistence.entity.UploadEntity;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.UploadJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    private final Path uploadPath;
    private final long maxFileSize;
    private final List<String> allowedTypes;
    private final UploadJpaRepository uploadRepository;

    public FileUploadService(
            @Value("${application.upload.directory}") String uploadDirectory,
            @Value("${application.upload.max-file-size}") long maxFileSize,
            @Value("${application.upload.allowed-types}") String allowedTypes,
            UploadJpaRepository uploadRepository
    ) {
        this.uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        this.maxFileSize = maxFileSize;
        this.allowedTypes = Arrays.asList(allowedTypes.split(","));
        this.uploadRepository = uploadRepository;

        try {
            Files.createDirectories(this.uploadPath);
            logger.info("Upload directory created/verified: {}", this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String uploadFile(MultipartFile file, Long usuarioId) {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            UploadEntity upload = new UploadEntity();
            upload.setNombreArchivo(uniqueFilename);
            upload.setNombreOriginal(originalFilename);
            upload.setTipoContenido(file.getContentType());
            upload.setTamanio(file.getSize());
            upload.setRuta(targetLocation.toString());
            upload.setUsuarioId(usuarioId);

            uploadRepository.save(upload);

            logger.info("File uploaded successfully: {} by user {}", uniqueFilename, usuarioId);
            return "/api/v1/uploads/" + uniqueFilename;

        } catch (IOException e) {
            throw new BusinessRuleViolationException("Failed to store file: " + e.getMessage());
        }
    }

    public Path getFilePath(String filename) {
        return uploadPath.resolve(filename).normalize();
    }

    @Transactional
    public void deleteFile(String filename) {
        try {
            Path filePath = getFilePath(filename);
            Files.deleteIfExists(filePath);
            uploadRepository.deleteByNombreArchivo(filename);
            logger.info("File deleted: {}", filename);
        } catch (IOException e) {
            logger.error("Failed to delete file: {}", filename, e);
            throw new BusinessRuleViolationException("Failed to delete file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessRuleViolationException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new BusinessRuleViolationException(
                    String.format("File size exceeds maximum allowed size of %d bytes", maxFileSize)
            );
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new BusinessRuleViolationException(
                    "File type not allowed. Allowed types: " + String.join(", ", allowedTypes)
            );
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
}
