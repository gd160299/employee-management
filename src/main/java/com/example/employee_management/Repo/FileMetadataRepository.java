package com.example.employee_management.Repo;

import com.example.employee_management.Common.CallStoredProcedureCommon;
import com.example.employee_management.Dto.FileMetadataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
		parameters.put("p_public_id", dto.getPublicId());
		parameters.put("p_department_id", dto.getDepartmentId());
		storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.SAVE_FILE", parameters);
	}

	public Optional<FileMetadataDto> findById(Long fileId) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("p_file_id", fileId);
		FileMetadataDto result = storedProcedureUtil.callStoredProcedureForSingleResult("PKG_EMPLOYEE.FIND_FILE_BY_ID",
				parameters, FileMetadataDto.class);
		return Optional.ofNullable(result);
	}

	public List<FileMetadataDto> search(Long departmentId, int pageBegin, int pageSize) {
		Map<String, Object> parameters = new LinkedHashMap<>();
		parameters.put("p_department_id", departmentId);
		parameters.put("p_page_begin", pageBegin);
		parameters.put("p_page_size", pageSize);
		return this.storedProcedureUtil.callStoredProcedureWithRefCursor("PKG_EMPLOYEE.FILE_SEARCH_BY_DEPARTMENT_ID",
				parameters, FileMetadataDto.class);
	}

    public void delete(Long fileId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("P_FILE_ID", fileId);
        this.storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.DELETE_FILE_BY_ID", parameters);
    }
}
