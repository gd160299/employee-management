package com.example.employee_management.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Dto.FileMetadataDto;
import com.example.employee_management.Repo.EmployeeRepository;
import com.example.employee_management.Repo.FileMetadataRepository;
import com.example.employee_management.Util.BusinessException;
import com.example.employee_management.Util.ErrorCode;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Transactional
    public Boolean uploadFile(MultipartFile file, Long employeeId, Long departmentId) throws IOException {
        String fileName = file.getOriginalFilename();
        String publicId = fileName != null ? fileName.substring(0, fileName.lastIndexOf('.')) : "";

        Map<String, Object> uploadParams = new HashMap<>();
        uploadParams.put("public_id", fileName);
        uploadParams.put("resource_type", "auto");
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
        FileMetadataDto metadata = new FileMetadataDto();
        metadata.setEmployeeId(employeeId);
        metadata.setDepartmentId(departmentId);
        metadata.setFileName(file.getOriginalFilename());
        String fileType = this.getFileExtension(file.getOriginalFilename());
        metadata.setFileType(fileType);
        metadata.setFileSize(file.getSize());
        metadata.setFilePath(uploadResult.get("url").toString());
        metadata.setPublicId(uploadResult.get("public_id").toString());
        this.fileMetadataRepository.save(metadata);
        return true;
    }

    public ResponseEntity<Resource> downloadFile(Long fileId) {
        FileMetadataDto fileMetadata = this.fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        String publicId = fileMetadata.getPublicId();
        String fileName = fileMetadata.getFileName();
        String filePath = fileMetadata.getFilePath();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        EmployeeDto emp = this.employeeService.findByUserName(currentUserName);

        if (!isAdmin && !hasAccess(emp, fileMetadata)) {
            throw new BusinessException(ErrorCode.FILE_CAN_NOT_ACCESS);
        }
        try {

            // Get a ByteArrayResource from the URL
            URL url = new URL(filePath);
            InputStream inputStream = url.openStream();
            byte[] out = IOUtils.toByteArray(inputStream);
            ByteArrayResource resource = new ByteArrayResource(out);

            // Create the headers
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=\"" + fileName + "\"");
            responseHeaders.add("Content-Type", "application/octet-stream");

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .contentLength(out.length)
                    .body(resource);

        } catch (Exception ex) {
            throw new RuntimeException("Failed to download the file: " + publicId, ex);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean hasAccess(EmployeeDto emp, FileMetadataDto fileMetadata) {
        String userRole = emp.getRoleName();

        if (userRole == null) {
            return false;
        }
        return switch (userRole) {
            case "MANAGER_DEV" -> fileMetadata.getDepartmentId() == 4;
            case "MANAGER_HR" -> fileMetadata.getDepartmentId() == 5;
            case "MANAGER_FINANCE" -> fileMetadata.getDepartmentId() == 6;
            case "USER" -> emp.getDepartmentId().equals(fileMetadata.getDepartmentId());
            default -> false;
        };
    }
}
