package com.example.employee_management.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dtbiuwlz6",
                "api_key", "123228379978194",
                "api_secret", "TikQcpk2f4k_0_FH7eUIiT6ZxpU",
                "secure", true));
    }
}