package com.candyshop.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.candyshop.entity.Role;
import com.candyshop.entity.User;

public class CustomUserDetails implements UserDetails {
	private final User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	// Trả về danh sách các quyền (vai trò) của người dùng.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet());
	}

	// Trả về mật khẩu đã được hash của người dùng.
	@Override
	public String getPassword() {
		return user.getPasswordHash();
	}

	// Trả về tên đăng nhập của người dùng (trong trường hợp này là email).
	@Override
	public String getUsername() {
		return user.getEmail();
	}

	// Kiểm tra tài khoản có hết hạn không (luôn trả về true).
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// Kiểm tra tài khoản có bị khóa không (luôn trả về true).
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// Kiểm tra thông tin xác thực (mật khẩu) có hết hạn không (luôn trả về true).
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Kiểm tra tài khoản có được kích hoạt không.
	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}

	// Phương thức tiện ích để lấy đối tượng User gốc.
	public User getUser() {
		return user;
	}
}
