package com.project.plantcare.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.plantcare.entity.RefreshToken;
import com.project.plantcare.entity.User;
import com.project.plantcare.repository.RefreshTokenRepository;
import com.project.plantcare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	public Optional<RefreshToken> getRefreshToken(String userId) {
		Optional<User> userOptional = userRepository.findByUserId(userId);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return refreshTokenRepository.findByUser(user);
		} else {
			return Optional.empty();
		}
	}

	public void deleteRefreshToken(String token) {
		Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
		refreshTokenOptional.ifPresent(refreshTokenRepository::delete);
	}
}
