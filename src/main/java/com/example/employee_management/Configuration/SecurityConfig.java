package com.example.employee_management.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.example.employee_management.Util.EnumRoles.*;

@Configuration
@EnableAsync
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173", "http://localhost:5173/", "http://localhost:8687", "http://localhost:8686")); // Add your frontend origin here
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource()) // Add this line to use the CORS configuration
                .and()
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
                .antMatchers(HttpMethod.POST, "/files/upload").hasAnyAuthority(ADMIN.getText(), MANAGER_DEV.getText(), MANAGER_HR.getText(), MANAGER_FINANCE.getText())
                .antMatchers(HttpMethod.GET, "/files/download").hasAnyAuthority(ADMIN.getText(), MANAGER_DEV.getText(), MANAGER_HR.getText(), MANAGER_FINANCE.getText(), USER.getText())
                .antMatchers(HttpMethod.GET, "/files/search").hasAnyAuthority(ADMIN.getText(), MANAGER_DEV.getText(), MANAGER_HR.getText(), MANAGER_FINANCE.getText(), USER.getText())
                .antMatchers(HttpMethod.DELETE, "/files/**").hasAnyAuthority(ADMIN.getText(), MANAGER_DEV.getText(), MANAGER_HR.getText(), MANAGER_FINANCE.getText())
                .antMatchers(HttpMethod.GET, "/employee/search").hasAnyAuthority(ADMIN.getText(), MANAGER.getText(), MANAGER_DEV.getText(), MANAGER_HR.getText(), MANAGER_FINANCE.getText())
                .antMatchers(HttpMethod.POST, "/employee/**").hasAuthority(ADMIN.getText())
                .antMatchers(HttpMethod.PUT, "/employee/change-password").hasAnyAuthority(ADMIN.getText(), MANAGER.getText(), USER.getText())
                .antMatchers(HttpMethod.PUT, "/employee/**").hasAuthority(ADMIN.getText())
                .antMatchers(HttpMethod.DELETE, "/employee/**").hasAuthority(ADMIN.getText())
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
