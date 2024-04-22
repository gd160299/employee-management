package com.example.employee_management.Configuration;

import com.example.employee_management.Dto.EmployeeDeviceDto;
import com.example.employee_management.Repo.EmployeeRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "allowed")
public class AllowedNetworkConfig {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Setter
    @Getter
    private List<String> ips = new ArrayList<>();

    private final List<String> macAddresses = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Lấy danh sách MAC
        List<EmployeeDeviceDto> devices = employeeRepository.getLstMac();
        for (EmployeeDeviceDto device : devices) {
            if (device.getMac() != null) {
                macAddresses.add(device.getMac());
            }
        }
    }

    public List<String> getMac() {
        return macAddresses;
    }

}

