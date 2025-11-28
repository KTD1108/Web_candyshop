package com.candyshop.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.candyshop.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
// Lớp cấu hình chính cho Spring Security, quản lý các quy tắc bảo mật của ứng dụng.
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	@Autowired
	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	// Cấu hình chuỗi bộ lọc bảo mật (security filter chain), đây là nơi định nghĩa các quy tắc truy cập.
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
			throws Exception {
		http.csrf(csrf -> csrf.disable()) // Tắt tính năng chống tấn công CSRF vì dùng JWT (stateless)
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Áp dụng cấu hình CORS
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Cấu hình session thành STATELESS, không lưu trạng thái phía server
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/**").permitAll()) // Cho phép tất cả các request (sẽ được cấu hình chi tiết hơn ở controller)
				.authenticationProvider(authenticationProvider)
				// Thêm bộ lọc JWT để kiểm tra token trước bộ lọc mặc định của Spring Security
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	// Cung cấp bean để mã hóa mật khẩu, sử dụng thuật toán BCrypt.
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	// Cấu hình CORS (Cross-Origin Resource Sharing) để cho phép các client từ nguồn khác (ví dụ: frontend) có thể gọi API.
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration c = new CorsConfiguration();
		c.setAllowedOrigins(List.of("*")); // Cho phép tất cả các nguồn
		c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // Cho phép tất cả các phương thức HTTP
		c.setAllowedHeaders(List.of("*")); // Cho phép tất cả các header
		UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
		src.registerCorsConfiguration("/**", c); // Áp dụng cấu hình cho tất cả các đường dẫn
		return src;
	}
}
