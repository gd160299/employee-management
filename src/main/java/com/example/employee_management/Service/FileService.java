package com.example.employee_management.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.employee_management.Dto.FileMetadataDto;
import com.example.employee_management.Repo.EmployeeRepository;
import com.example.employee_management.Repo.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Service
public class FileService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Boolean uploadFile(MultipartFile file, Long employeeId) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
        FileMetadataDto metadata = new FileMetadataDto();
        metadata.setEmployeeId(employeeId);
        metadata.setFileName(file.getOriginalFilename());
        String fileType = this.getFileExtension(file.getOriginalFilename());
        metadata.setFileType(fileType);
        metadata.setFileSize(file.getSize());
        metadata.setFilePath(uploadResult.get("url").toString());
        this.fileMetadataRepository.save(metadata);
        return true;
    }

    public Resource downloadFile(Long fileId) {
        FileMetadataDto fileMetadata = this.fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        String filePath = fileMetadata.getFilePath();
        try {
            URL url = new URL(filePath);
            return new InputStreamResource(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
