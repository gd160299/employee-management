package com.example.employee_management.Service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public List<GrantedAuthority> getRolesFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(String::toUpperCase) // Chuyển đổi tất cả các vai trò sang chữ hoa
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            // log: cấu trúc JWT không hợp lệ
        } catch (ExpiredJwtException ex) {
            // log: JWT đã hết hạn
        } catch (UnsupportedJwtException ex) {
            // log: JWT không được hỗ trợ
        } catch (IllegalArgumentException ex) {
            // log: chuỗi claims JWT trống
        }
        return false;
    }

}
