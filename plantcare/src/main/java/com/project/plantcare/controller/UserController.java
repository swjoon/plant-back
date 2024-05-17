package com.project.plantcare.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.plantcare.config.SecurityUtil;
import com.project.plantcare.dto.LoginDTO;
import com.project.plantcare.dto.TokenDTO;
import com.project.plantcare.dto.TokenResponseDTO;
import com.project.plantcare.dto.UserDTO;
import com.project.plantcare.dto.UserInfoDTO;
import com.project.plantcare.entity.RefreshToken;
import com.project.plantcare.service.RefreshTokenService;
import com.project.plantcare.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Log4j2
public class UserController {

	private final UserService userService;
	private final RefreshTokenService refreshTokenService;

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO loginDTO) {
		log.info(loginDTO);
		TokenDTO tokenDTO = userService.login(loginDTO);
		ResponseCookie responseCookie = ResponseCookie.from("refresh_token", tokenDTO.getRefreshToken()).httpOnly(true)
				.secure(true).sameSite("None").maxAge(tokenDTO.getDuration()).path("/").build();
		String nickName = userService.getNickName(loginDTO.getUserId());
		String role = userService.getRole(loginDTO.getUserId());
		TokenResponseDTO tokenResponseDTO = TokenResponseDTO.builder().isNewMember(false).nickName(nickName).role(role)
				.accessToken(tokenDTO.getAccessToken()).refreshToken(tokenDTO.getRefreshToken()).build();
		return ResponseEntity.ok().header("Set-Cookie", responseCookie.toString()).body(tokenResponseDTO);
	}

	// 회원가입
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
		try {
			userService.save(userDTO);
			return ResponseEntity.ok("회원가입 완료");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
		}
	}

	@PostMapping("/logout")
	public void logout(@RequestBody String refreshToken) {
		System.out.println("refreshtoken: " + refreshToken);
		String userId = SecurityUtil.getCurrentUsername();
		Optional<RefreshToken> tokenOptional = refreshTokenService.getRefreshToken(userId);
		if (tokenOptional.isPresent()) {
			RefreshToken token = tokenOptional.get();
			System.out.println("token: " + token.getToken());
			if (refreshToken.equals(token.getToken())) {
				refreshTokenService.deleteRefreshToken(refreshToken);
			} else {
				throw new IllegalArgumentException("잘못된 리프레시 토큰입니다.");
			}
		} else {
			// 유효하지 않은 사용자 ID
			throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
		}
	}

	@GetMapping("/userinfo")
	public ResponseEntity<UserInfoDTO> getUserInfo() {

		String userId = SecurityUtil.getCurrentUsername();

		System.out.println(userId);

		UserInfoDTO userInfoDTO = userService.getUserInfo(userId);

		System.out.println(userInfoDTO);

		return ResponseEntity.ok(userInfoDTO);

	}

}
