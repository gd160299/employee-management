package com.example.employee_management.Service;

import com.example.employee_management.Dto.AuthenticationDto;
import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Dto.EmployeeRoleDto;
import com.example.employee_management.Repo.EmployeeRepository;
import com.example.employee_management.Repo.OtpRepository;
import com.example.employee_management.Util.BusinessException;
import com.example.employee_management.Util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OTPService otpService;

    @Transactional
    public boolean authenticateEmployee(String username, String password) {
        return employeeRepository.findByUserName(username)
                .filter(employee -> passwordEncoder.matches(password, employee.getPassWord()))
                .map(employee -> {
                    String otp = otpService.generateOTP(username);
                    this.saveOtp(username, otp, employee.getEmployeeId(), employee.getEmail());
                    return true;
                }).orElse(false);
    }

    public List<String> getUserRoles(String userName) {
        EmployeeDto employee = this.employeeRepository.findByUserName(userName).orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND, userName));
        Long employeeId = employee.getEmployeeId();
        List<EmployeeRoleDto> roles = this.employeeRepository.getUserRoles(employeeId);
        return roles.stream().map(EmployeeRoleDto::getRoleName).collect(Collectors.toList());
    }

    public boolean verifyOtp(String username, String otp) {
        Optional<AuthenticationDto> authOpt = otpRepository.findByUserName(username);
        if (authOpt.isPresent()) {
            AuthenticationDto auth = authOpt.get();
            // Kiểm tra xem OTP có quá 1 phút không
            if (new Date().getTime() - auth.getCreatedTime().getTime() > 180000) {
                // OTP quá hạn
                otpRepository.delete(auth.getAuthId());
                return false; // OTP không hợp lệ do quá hạn
            }
            if (auth.getOtp().equals(otp)) {
                otpRepository.delete(auth.getAuthId()); // Xóa OTP sau khi đã được sử dụng
                return true; // OTP hợp lệ
            }
        }
        return false;
    }

    public void regenerateOtp(String username) {
        employeeRepository.findByUserName(username).ifPresent(employee -> {
            String newOtp = otpService.generateOTP(username);
            this.saveOtp(username, newOtp, employee.getEmployeeId(), employee.getEmail());
        });
    }

    private void saveOtp(String username, String otp, Long employeeId, String email) {
        // Kiểm tra và xóa OTP cũ nếu có
        Optional<AuthenticationDto> auth = otpRepository.findByUserName(username);
        auth.ifPresent(a -> otpRepository.delete(a.getAuthId()));

        // Tạo và lưu OTP mới
        AuthenticationDto newAuth = new AuthenticationDto();
        newAuth.setEmployeeId(employeeId);
        newAuth.setUserName(username);
        newAuth.setOtp(otp);
        newAuth.setCreatedTime(new Date());
        otpRepository.save(newAuth);

        // Gửi OTP qua email
        otpService.sendOtpViaEmail(email, otp);
    }

}
