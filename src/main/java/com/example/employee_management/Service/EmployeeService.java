package com.example.employee_management.Service;

import com.example.employee_management.Dto.DepartmentDto;
import com.example.employee_management.Dto.EmployeeDeviceDto;
import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Dto.RolesDto;
import com.example.employee_management.Model.LoginRequest;
import com.example.employee_management.Repo.EmployeeRepository;
import com.example.employee_management.Util.BusinessException;
import com.example.employee_management.Util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${defaults.password}")
    private String defaultPassword;

    public EmployeeDto findByUserName(String userName) {
        return this.employeeRepository.findByUserName(userName).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userName));
    }

    @Transactional
    public List<EmployeeDto> search(Long departmentId, String employeeName, String userName, int pageBegin, int pageSize) {
        return this.employeeRepository.search(departmentId, employeeName, userName, pageBegin, pageSize);
    }

    public void create(EmployeeDto objInput) {
        this.employeeRepository.findByUserName(objInput.getUserName())
                .ifPresent(user -> {
                    throw new BusinessException(ErrorCode.USER_EXIST, objInput.getUserName());
                });
        objInput.setPassWord(this.encodePassWord(this.defaultPassword));
        this.employeeRepository.create(objInput);
    }
    @Transactional
    public void update(EmployeeDto objInput) {
        this.employeeRepository.findByUserName(objInput.getUserName()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST));
        objInput.setPassWord(this.encodePassWord(objInput.getPassWord()));
        this.employeeRepository.update(objInput);
    }

    public void changePassword(LoginRequest objInput) {
        EmployeeDto employee = this.findByUserName(objInput.getUsername());
        if (!passwordEncoder.matches(objInput.getOldPassword(), employee.getPassWord())) {
            throw new BusinessException(ErrorCode.OLD_PASSWORD_IS_NOT_CORRECT);
        }
        objInput.setPassword(this.encodePassWord(objInput.getPassword()));
        this.employeeRepository.changePassword(objInput);
    }

    public void resetPassword(LoginRequest objInput) {
        this.findByUserName(objInput.getUsername());
        objInput.setPassword(this.encodePassWord(this.defaultPassword));
        this.employeeRepository.changePassword(objInput);
    }
    @Transactional
    public void delete(String userName) {
        EmployeeDto employee = this.findByUserName(userName);
        this.employeeRepository.delete(employee.getEmployeeId());
    }

    public List<DepartmentDto> getLstDepartment() {
        return this.employeeRepository.getLstDepartment();
    }

    public List<EmployeeDeviceDto> getLstMacDevice() {
        return this.employeeRepository.getLstMac();
    }

    public List<RolesDto> getLstRoles() {
        return this.employeeRepository.getLstRoles();
    }

    private String encodePassWord(String passWord) {
        return this.passwordEncoder.encode(passWord);
    }
}
