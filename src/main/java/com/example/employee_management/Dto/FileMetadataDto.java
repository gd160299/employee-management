package com.example.employee_management.Dto;

import com.example.employee_management.Common.DbColumnMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FileMetadataDto {
    @DbColumnMapper("File_ID")
    private Long fileId;

    @DbColumnMapper("Employee_ID")
    private Long employeeId;

    @DbColumnMapper("File_Name")
    private String fileName;

    @DbColumnMapper("File_Type")
    private String fileType;

    @DbColumnMapper("File_Size")
    private Long fileSize;

    @DbColumnMapper("Upload_Date")
    private Date uploadDate;

    @DbColumnMapper("File_Path")
    private String filePath;
}
