package com.example.employee_management.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không sử dụng session
                .and()
                .addFilterBefore(new CustomAccessFilter(), UsernamePasswordAuthenticationFilter.class) // Chạy CustomAccessFilter trước các bộ lọc khác
                // Yêu cầu HTTPS cho tất cả các request
//                .requiresChannel()
//                .anyRequest().requiresSecure()
//                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/v2/api-docs", "/swagger-resources/**", "/webjars/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/employee/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/employee/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/employee/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
