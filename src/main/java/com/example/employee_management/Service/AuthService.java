package com.example.employee_management.Service;

import com.example.employee_management.Dto.AuthenticationDto;
import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Repo.EmployeeRepository;
import com.example.employee_management.Repo.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        Optional<EmployeeDto> employee = employeeRepository.findByUserName(username);
        if (employee.isPresent() && passwordEncoder.matches(password, employee.get().getPassWord())) {
            // Mật khẩu đúng, sinh OTP và gửi qua SMS
            String otp = otpService.generateOTP(username);
            otpService.sendOtpViaEmail(employee.get().getEmail(), otp);

            // Lưu OTP vào cơ sở dữ liệu
            AuthenticationDto authentication = new AuthenticationDto();
            authentication.setEmployeeId(employee.get().getEmployeeId());
            authentication.setUserName(username);
            authentication.setOtp(otp);
            otpRepository.save(authentication);

            return true;
        }
        return false;
    }

    public boolean verifyOtp(String username, String otp) {
        Optional<AuthenticationDto> authOpt = otpRepository.findByUserName(username);
        if (authOpt.isPresent()) {
            AuthenticationDto auth = authOpt.get();
            if (auth.getOtp().equals(otp)) {
                // OTP đúng, xóa OTP cũ và cho phép người dùng tiếp tục
                otpRepository.delete(auth.getAuthId());
                return true;
            }
        }
        return false;
    }
}
