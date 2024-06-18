package com.example.employee_management.RestController;

import com.example.employee_management.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("employeeId") Long employeeId, @RequestParam("departmentId") Long departmentId) {
        try {
            this.fileService.uploadFile(file, employeeId, departmentId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam Long fileId) {
        return fileService.downloadFile(fileId);
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<?> deleteFile(@RequestParam Long fileId) {
        this.fileService.delete(fileId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam Long departmentId, @RequestParam int pageBegin, @RequestParam int pageSize) {
        return new ResponseEntity<>(this.fileService.search(departmentId, pageBegin, pageSize), HttpStatus.OK);
    }
}
