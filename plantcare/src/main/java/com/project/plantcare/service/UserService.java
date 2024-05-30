package com.project.plantcare.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.plantcare.dto.ChangePwDTO;
import com.project.plantcare.dto.FindPwDTO;
import com.project.plantcare.dto.LoginDTO;
import com.project.plantcare.dto.MailDTO;
import com.project.plantcare.dto.TokenDTO;
import com.project.plantcare.dto.UserDTO;
import com.project.plantcare.dto.UserInfoDTO;
import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.Role;
import com.project.plantcare.entity.User;
import com.project.plantcare.entity.UserDevice;
import com.project.plantcare.repository.DeviceRepository;
import com.project.plantcare.repository.RefreshTokenRepository;
import com.project.plantcare.repository.UserDeviceRepository;
import com.project.plantcare.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;
	private final DeviceRepository deviceRepository;
	private final UserDeviceRepository userDeviceRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final TokenService tokenService;
	private final AuthService authService;
	private final SendService sendService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		return userRepository.findByUserId(userId).map(this::createUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("userId: " + userId + "를 데이터베이스에서 찾을 수 없습니다."));
	}

	private UserDetails createUserDetails(User user) {
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRoles().name());

		return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPw(),
				Collections.singleton(grantedAuthority));
	}

	// 로그인
	@Transactional
	public TokenDTO login(LoginDTO loginDTO) {
		authService.authenticateLogin(loginDTO);

		User user = userRepository.findByUserId(loginDTO.getUserId()).get();
		return tokenService.createToken(user);
	}

	// 회원가입
	public void save(UserDTO userDTO) {
		validateDuplicatieUser(userDTO); // 중복 아이디 검출
		String encodedPW = bCryptPasswordEncoder.encode(userDTO.getUserPw());
		userDTO.setUserPw(encodedPW);

		User user = toUserEntity(userDTO);

		user.updateRole(Role.ROLE_USER);

		userRepository.save(user);
	}

	public Optional<User> getUser(String userId) {
		return userRepository.findByUserId(userId);
	}

	public String getRole(String userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		User user = optionalUser.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
		String role = user.getRoles().toString().substring(5);
		return role;
	}

	public String getNickName(String userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		User user = optionalUser.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
		String nickName = user.getNickname();
		return nickName;
	}

	public UserInfoDTO getUserInfo(String userId) {
		Optional<User> user = userRepository.findByUserId(userId);

		UserInfoDTO userInfoDTO = UserInfoDTO.builder().userId(user.get().getUserId())
				.username(user.get().getUsername()).nickname(user.get().getNickname()).birth(user.get().getBirth())
				.createdDate(user.get().getCreatedDate()).build();

		return userInfoDTO;
	}

	// 비밀번호 변경
	public void changePw(ChangePwDTO dto, String userId) {
		Optional<User> OptionalUser = userRepository.findById(userId);
		User user = OptionalUser.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with userId: " + userId));
		if (bCryptPasswordEncoder.matches(dto.getUserPw(), user.getUserPw())) {
			String newPassword = bCryptPasswordEncoder.encode(dto.getUserNewPw());
			user.updatePassword(newPassword);
			userRepository.save(user);
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다.");
		}
	}

	// 닉네임 변경
	public void changeNickName(String userId, String nickName) {
		Optional<User> OptionalUser = userRepository.findById(userId);
		User user = OptionalUser.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with userId: " + userId));
		user.updateNickName(nickName);
		userRepository.save(user);
	}

	// 회원 탈퇴
	@Transactional
	public void deleteUser(String userId) {
		Optional<User> OptionalUser = userRepository.findById(userId);
		User user = OptionalUser.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with userId: " + userId));
		
		Optional<List<UserDevice>> OptionalList = userDeviceRepository.findByUser(user);
		if(OptionalList.isPresent()) {
			List<UserDevice> lists = OptionalList.get();
			for(UserDevice list: lists) {
				Device device = list.getDevice();
				device.setDeviceUse(false);
				deviceRepository.save(device);
			}
		}
		refreshTokenRepository.deleteByUser(user);
		userRepository.delete(user);
		
	}
	
	
	// email 보내기 전 유저 정보 체크
	public void checkUserInfo(FindPwDTO pwDTO) {
		Optional<User> OptionalUser = userRepository.findById(pwDTO.getUserId());
		User user = OptionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"User not found with userId: " + pwDTO.getUserId()));

		String formattedBirth = user.getBirth().toString().substring(0, 4) + user.getBirth().toString().substring(5, 7)
				+ user.getBirth().toString().substring(8, 10);
		System.out.println(pwDTO);
		if (pwDTO.getUserName().equals(user.getUsername()) && pwDTO.getBirth().equals(formattedBirth)) {
			System.out.println("성공");
			sendEmail(pwDTO);
		} else {
			System.out.println("실패");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user information provided.");
		}
	}

	// email 보내기
	public void sendEmail(FindPwDTO pwDTO) {
		MailDTO dto = sendService.createMailAndChargePassword(pwDTO);
		sendService.mailSend(dto);
	}
	
	// 중복회원 체크
	private void validateDuplicatieUser(UserDTO userDTO) {
		userRepository.findById(userDTO.getUserId()).ifPresent(u -> {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		});
	}

	private User toUserEntity(UserDTO userDTO) {
		return User.builder().userId(userDTO.getUserId()).userPw(userDTO.getUserPw()).username(userDTO.getUsername())
				.nickname(userDTO.getNickname()).birth(userDTO.getBirth()).build();
	}
}