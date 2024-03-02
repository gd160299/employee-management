package com.example.employee_management.Repo;

import com.example.employee_management.Common.CallStoredProcedureCommon;
import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Dto.EmployeeRoleDto;
import com.example.employee_management.Dto.RolesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.sql.SQLException;
import java.util.*;

@Repository
public class EmployeeRepository {
    @Autowired
    private CallStoredProcedureCommon storedProcedureUtil;

    public Optional<EmployeeDto> findByUserName(String userName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_user_name", userName);
        EmployeeDto result = this.storedProcedureUtil.callStoredProcedureForSingleResult("PKG_EMPLOYEE.FIND_EMPLOYEE_BY_USER_NAME", parameters, EmployeeDto.class);
        return Optional.ofNullable(result);
    }

    public Optional<EmployeeDto> findById(Long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_id", id);
        EmployeeDto result = this.storedProcedureUtil.callStoredProcedureForSingleResult("PKG_EMPLOYEE.find_employee_by_id", parameters, EmployeeDto.class);
        return Optional.ofNullable(result);
    }

    public List<EmployeeRoleDto> getUserRoles(Long employeeId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_id", employeeId);
        return this.storedProcedureUtil.callStoredProcedureWithRefCursor("PKG_EMPLOYEE.GET_USER_ROLES_BY_EMPLOYEE_ID", parameters, EmployeeRoleDto.class);
    }

    public List<EmployeeDto> search(Long departmentId, String employeeName, String userName, int pageBegin, int pageEnd) {
            Map<String, Object> parameters = new LinkedHashMap<>();
            parameters.put("p_department_id", departmentId);
            parameters.put("p_employee_name", employeeName);
            parameters.put("p_user_name", userName);
            parameters.put("p_page_begin", pageBegin);
            parameters.put("p_page_end", pageEnd);
            return this.storedProcedureUtil.callStoredProcedureWithRefCursor("PKG_EMPLOYEE.employee_search", parameters, EmployeeDto.class);
    }

    public void create(EmployeeDto objInput) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_name", objInput.getEmployeeName());
        parameters.put("p_department_id", objInput.getDepartmentId());
        parameters.put("p_salary", objInput.getSalary());
        parameters.put("p_user_name", objInput.getUserName());
        parameters.put("p_phone_number", objInput.getPhoneNumber());
        parameters.put("p_position", objInput.getPosition());
        parameters.put("p_email", objInput.getEmail());
        parameters.put("p_pass_word", objInput.getPassWord());
        this.storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.save_employee", parameters);
    }

    public void update(EmployeeDto objInput) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_employee_id", objInput.getEmployeeId());
        parameters.put("p_employee_name", objInput.getEmployeeName());
        parameters.put("p_department_id", objInput.getDepartmentId());
        parameters.put("p_salary", objInput.getSalary());
        parameters.put("p_user_name", objInput.getUserName());
        parameters.put("p_phone_number", objInput.getPhoneNumber());
        parameters.put("p_position", objInput.getPosition());
        parameters.put("p_email", objInput.getEmail());
        parameters.put("p_pass_word", objInput.getPassWord());
        this.storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.update_employee", parameters);
    }

    public void delete(String userName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_user_name", userName);
        this.storedProcedureUtil.callStoredProcedure("PKG_EMPLOYEE.delete_employee", parameters);
    }
}
