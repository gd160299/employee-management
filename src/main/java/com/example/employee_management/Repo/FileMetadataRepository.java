package com.example.employee_management.Repo;

import com.example.employee_management.Common.CallStoredProcedureCommon;
import com.example.employee_management.Dto.AuthenticationDto;
import com.example.employee_management.Dto.FileMetadataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class FileMetadataRepository {

    @Autowired
    private CallStoredProcedureCommon storedProcedureUtil;

    public void save(FileMetadataDto dto) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_id", dto.getEmployeeId());
        parameters.put("p_file_name", dto.getFileName());
        parameters.put("p_file_type", dto.getFileType());
        parameters.put("p_file_size", dto.getFileSize());
        parameters.put("p_file_path", dto.getFilePath());
        storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.SAVE_FILE", parameters);
    }

    public Optional<FileMetadataDto> findById(Long fileId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_file_id", fileId);
        FileMetadataDto result = storedProcedureUtil.callStoredProcedureForSingleResult("PKG_EMPLOYEE.FIND_FILE_BY_ID", parameters, FileMetadataDto.class);
        return Optional.ofNullable(result);
    }
}
