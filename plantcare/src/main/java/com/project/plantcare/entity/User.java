package com.project.plantcare.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "User")
@NoArgsConstructor
@Getter
public class User {

	@Id
    @Column(name = "user_id",unique = true)
    private String userId;

	@Column(name = "user_pw")
    private String userPw;
	
	@Column(name = "username")
	private String username;
	
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birth")
    private LocalDate birth;
    
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role roles;
    
    @CreatedDate
    @Column(name = "createddate", updatable = false)
    private LocalDateTime createdDate;
    

    @Builder
    public User(String userId, String userPw, String username, String nickname, LocalDate birth, Role roles, LocalDateTime createdDate) {
    	this.userId = userId;
    	this.userPw = userPw;
    	this.username = username;
    	this.nickname = nickname;
    	this.birth = birth;
    	this.roles = roles;
    	this.createdDate = LocalDateTime.now();
    }
    
   
    public void updateRole(Role roles) {
        this.roles = roles;
    }
    
    public void updatePassword(String userPw) {
    	this.userPw = userPw;
    }
    
    public void updateNickName(String nickname) {
    	this.nickname = nickname;
    }
}
