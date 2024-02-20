package com.example.employee_management.RestController;

import com.example.employee_management.Dto.EmployeeDto;
import com.example.employee_management.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeRestController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/find-by-user-name")
    public ResponseEntity<EmployeeDto> findByUserName(@RequestParam String userName) {
        return ResponseEntity.ok(this.employeeService.findByUserName(userName));
    }

    @PostMapping("/create")
    public void create(@RequestBody EmployeeDto objInput) {
        this.employeeService.create(objInput);
    }

    @PutMapping("/update")
    public void update(@RequestBody EmployeeDto objInput) {
        this.employeeService.update(objInput);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String userName) {
        this.employeeService.delete(userName);
    }
}
