package com.candyshop.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.candyshop.entity.User;
import com.candyshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
//  Chịu trách nhiệm tải thông tin người dùng từ cơ sở dữ liệu dựa trên email.
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	// tìm kiếm người dùng trong cơ sở dữ liệu bằng email và trả về một đối tượng
	// UserDetails.
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new CustomUserDetails(user);
	}
}
