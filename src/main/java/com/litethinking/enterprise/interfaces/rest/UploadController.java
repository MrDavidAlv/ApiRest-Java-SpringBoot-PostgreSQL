package com.litethinking.enterprise.interfaces.rest;

import com.litethinking.enterprise.application.service.FileUploadService;
import com.litethinking.enterprise.infrastructure.security.UserContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/uploads")
@Tag(name = "Uploads", description = "Endpoints for file upload management")
public class UploadController {

    private final FileUploadService fileUploadService;
    private final UserContextService userContextService;

    public UploadController(
            FileUploadService fileUploadService,
            UserContextService userContextService
    ) {
        this.fileUploadService = fileUploadService;
        this.userContextService = userContextService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file", description = "Upload an image file to the server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        Long usuarioId = userContextService.getCurrentUserId();
        String fileUrl = fileUploadService.uploadFile(file, usuarioId);

        Map<String, String> response = new HashMap<>();
        response.put("url", fileUrl);
        response.put("message", "File uploaded successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{filename:.+}")
    @Operation(summary = "Get uploaded file", description = "Retrieve an uploaded file by filename")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = fileUploadService.getFilePath(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{filename}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete file", description = "Delete an uploaded file (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String filename) {
        fileUploadService.deleteFile(filename);

        Map<String, String> response = new HashMap<>();
        response.put("message", "File deleted successfully");

        return ResponseEntity.ok(response);
    }
}
