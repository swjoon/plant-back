package com.project.plantcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.RefreshToken;
import com.project.plantcare.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUser(User user);
    
    Optional<RefreshToken> findByToken(String token); // 리프레시 토큰으로 검색
    
    void deleteByUser(User user); // 유저에 해당하는 리프레시 토큰 삭제
    
}