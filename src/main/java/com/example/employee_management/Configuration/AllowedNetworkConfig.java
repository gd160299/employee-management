package com.example.employee_management.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "allowed")
public class AllowedNetworkConfig {
    private List<String> ips = new ArrayList<>();

    private List<String> mac = new ArrayList<>();

    public List<String> getIps() {
        return ips;
    }

    public List<String> getMac() {
        return mac;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public void setMac(List<String> mac) {
        this.mac = mac;
    }
}

