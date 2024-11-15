package com.project.plantcare.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.project.plantcare.dto.TokenDTO;
import com.project.plantcare.entity.RefreshToken;
import com.project.plantcare.entity.User;
import com.project.plantcare.jwt.JwtTokenProvider;
import com.project.plantcare.repository.RefreshTokenRepository;
import com.project.plantcare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    
    public TokenDTO createToken(User user) {
        TokenDTO tokenDTO = tokenProvider.createTokenDTO(user.getUserId(), user.getRoles());
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenDTO.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDTO;
    }
    
    public TokenDTO refresh(TokenDTO tokenDTO) {
        if(!tokenProvider.validateToken(tokenDTO.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenDTO.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByUser(userRepository.findByUserId(authentication.getName()).get())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getToken().equals(tokenDTO.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 일치하지 않습니다.");
        }

        User user = userRepository.findByUserId(refreshToken.getUser().getUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));
        TokenDTO tokenDto = tokenProvider.createTokenDTO(user.getUserId(), user.getRoles());

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }
    
}

//    public TokenDTO createToken(UserDTO userDTO) {
//        TokenDTO tokenDTO = tokenProvider.createTokenDTO(userDTO.getUserId(),userDTO.getRoles());
//        User user = userRepository.findByUserId(userDTO.getUserId()).orElseThrow(() -> new RuntimeException("Wrong Access (member does not exist)"));
//        RefreshToken refreshToken = RefreshToken.builder()
//                .user(user)
//                .token(tokenDTO.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);
//
//        return tokenDTO;
//    }