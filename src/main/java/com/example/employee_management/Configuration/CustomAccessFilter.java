package com.example.employee_management.Configuration;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
public class CustomAccessFilter extends OncePerRequestFilter {

    private AllowedNetworkConfig allowedNetworkConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.allowedNetworkConfig = webApplicationContext.getBean(AllowedNetworkConfig.class);
        InetAddress address = InetAddress.getLocalHost();
        // Lấy địa chỉ IP
        String ip = address.getHostAddress();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
        byte[] macAddressBytes  = networkInterface.getHardwareAddress();
        StringBuilder macBuilder = new StringBuilder();
        for (int i = 0; i < macAddressBytes .length; i++) {
            // Ghép chuỗi để lấy ra địa chỉ MAC
            macBuilder.append(String.format("%02X%s", macAddressBytes [i], (i < macAddressBytes .length - 1) ? "-" : ""));
        }
        String currentMacAddress  = macBuilder.toString();
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalTime vnTime = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        boolean isAllowedTime = vnTime.isAfter(LocalTime.of(8, 0)) && vnTime.isBefore(LocalTime.of(17, 0));
        boolean isAllowedDay = dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
        boolean isAllowedIP = this.allowedNetworkConfig.getIps().stream().anyMatch(ip::startsWith);
        boolean isAllowedMAC = this.allowedNetworkConfig.getMac().stream().anyMatch(macAllowed -> macAllowed.equalsIgnoreCase(currentMacAddress));

        if ((isAllowedIP || isAllowedMAC) && isAllowedTime && isAllowedDay) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied");
        }
    }
}
