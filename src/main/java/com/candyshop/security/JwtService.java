package com.candyshop.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
//  chịu trách nhiệm cho tất cả các hoạt động liên quan đến JWT (JSON Web Token), bao gồm tạo, phân tích và xác thực token.
public class JwtService {

	// Khóa bí mật để ký token, được lấy từ tệp cấu hình.
	@Value("${app.jwt.secret}")
	private String secret;

	// Thời gian hết hạn của token, được lấy từ tệp cấu hình.
	@Value("${app.jwt.expiration}")
	private long expirationMs;

	// Tạo và trả về khóa ký (signing key) từ chuỗi bí mật đã được mã hóa Base64.
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secret.getBytes()));
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Trích xuất tên người dùng (subject) từ token.
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Trích xuất một claim cụ thể từ token bằng cách sử dụng một hàm resolver.
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Phân tích token và trích xuất tất cả các claims.
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	// Tạo một token mới cho người dùng.
	public String generateToken(UserDetails userDetails) {
		return generateToken(Map.of(), userDetails);
	}

	// Tạo một token mới với các claims bổ sung cho người dùng.
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + expirationMs);
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(now)
				.setExpiration(exp).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	// Kiểm tra xem token có hợp lệ không (tên người dùng khớp và chưa hết hạn).
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// Kiểm tra xem token đã hết hạn chưa.
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Trích xuất ngày hết hạn từ token.
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
