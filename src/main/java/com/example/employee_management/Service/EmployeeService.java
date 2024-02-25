package com.example.employee_management.Service;

import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Repo.EmployeeRepository;
import com.example.employee_management.Util.BusinessException;
import com.example.employee_management.Util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmployeeDto findByUserName(String userName) {
        return this.employeeRepository.findByUserName(userName).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userName));
    }

    public void create(EmployeeDto objInput) {
        this.employeeRepository.findByUserName(objInput.getUserName())
                .ifPresent(user -> {
                    throw new BusinessException(ErrorCode.USER_EXIST, objInput.getUserName());
                });
        objInput.setPassWord(this.encodePassWord(objInput.getPassWord()));
        this.employeeRepository.create(objInput);
    }

    public void update(EmployeeDto objInput) {
        this.employeeRepository.findById(objInput.getEmployeeId()).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST));
        objInput.setPassWord(this.encodePassWord(objInput.getPassWord()));
        this.employeeRepository.update(objInput);
    }

    public void delete(String userName) {
        this.findByUserName(userName);
        this.employeeRepository.delete(userName);
    }

    private String encodePassWord(String passWord) {
        return this.passwordEncoder.encode(passWord);
    }
}
