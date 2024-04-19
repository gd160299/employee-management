package com.example.employee_management.RestController;

import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Model.LoginRequest;
import com.example.employee_management.Service.EmployeeService;
import com.example.employee_management.Util.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeRestController {
    @Autowired
    private EmployeeService employeeService;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        // Create a response body, could be a Map or a custom DTO
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getErrorCode().toString());
        response.put("message", ex.getMessage());

        // You can choose an appropriate HTTP status
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/find-by-user-name")
    public ResponseEntity<?> findByUserName(@RequestParam String userName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN") ||
                        grantedAuthority.getAuthority().equals("MANAGER"));
        // Nếu người dùng không phải admin và yêu cầu thông tin của người dùng khác
        if (!isAdmin && !currentUserName.equals(userName)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(this.employeeService.findByUserName(userName));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) Long departmentId, @RequestParam(required = false) String employeeName, @RequestParam(required = false) String userName, @RequestParam int pageBegin, @RequestParam int pageSize) {
        return ResponseEntity.ok(this.employeeService.search(departmentId, employeeName, userName, pageBegin, pageSize));
    }

    @GetMapping("/get-lst-department")
    public ResponseEntity<?> getLstDepartment() {
        return ResponseEntity.ok(this.employeeService.getLstDepartment());
    }

    @PostMapping("/create")
    public void create(@RequestBody EmployeeDto objInput) {
        this.employeeService.create(objInput);
    }

    @PutMapping("/update")
    public void update(@RequestBody EmployeeDto objInput) {
        this.employeeService.update(objInput);
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody LoginRequest objInput) {
        this.employeeService.changePassword(objInput);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody LoginRequest objInput) {
        this.employeeService.resetPassword(objInput);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String userName) {
        this.employeeService.delete(userName);
    }
}
