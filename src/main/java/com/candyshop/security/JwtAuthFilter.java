package com.candyshop.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// thực thi một lần cho mỗi yêu cầu HTTP để kiểm tra và xác thực token JWT.
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;

	// xử lý logic xác thực JWT cho mỗi yêu cầu.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;

		// Nếu header Authorization không tồn tại hoặc không bắt đầu bằng "Bearer ", bỏ
		// qua và chuyển tiếp yêu cầu.
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Trích xuất token JWT từ header.
		jwt = authHeader.substring(7);
		try {
			// Trích xuất email người dùng từ token.
			userEmail = jwtService.extractUsername(jwt);
		} catch (Exception e) {
			// Nếu có lỗi khi trích xuất, chuyển tiếp yêu cầu.
			filterChain.doFilter(request, response);
			return;
		}

		// Nếu có email và chưa có phiên xác thực nào trong SecurityContextHolder...
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Tải thông tin người dùng từ cơ sở dữ liệu.
			UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
			// Kiểm tra xem token có hợp lệ không.
			if (jwtService.isTokenValid(jwt, userDetails)) {
				// Nếu hợp lệ, tạo một phiên xác thực và đặt nó vào SecurityContextHolder.
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// Chuyển tiếp yêu cầu đến bộ lọc tiếp theo trong chuỗi.
		filterChain.doFilter(request, response);
	}
}
