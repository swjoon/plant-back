package com.project.plantcare.service;

import java.util.Collections;
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

import com.project.plantcare.dto.LoginDTO;
import com.project.plantcare.dto.TokenDTO;
import com.project.plantcare.dto.UserDTO;
import com.project.plantcare.dto.UserInfoDTO;
import com.project.plantcare.entity.Role;
import com.project.plantcare.entity.User;
import com.project.plantcare.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	@Autowired
	private final UserRepository userRepository;
	private final TokenService tokenService;
	private final AuthService authService;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("userId: " + userId + "를 데이터베이스에서 찾을 수 없습니다."));
    }
	
    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRoles().name());

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getUserPw(),
                Collections.singleton(grantedAuthority)
        );
    }
    
    //로그인
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


	// 중복회원 체크
	private void validateDuplicatieUser(UserDTO userDTO) {
		userRepository.findById(userDTO.getUserId())
		.ifPresent( u -> {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		});
	}
	
	private User toUserEntity(UserDTO userDTO) {
		return User.builder()
				.userId(userDTO.getUserId())
				.userPw(userDTO.getUserPw())
				.username(userDTO.getUsername())
				.nickname(userDTO.getNickname())
				.birth(userDTO.getBirth())
				.build();
	}
}