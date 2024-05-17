package com.project.plantcare.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.plantcare.entity.Role;

import lombok.Data;

@Data
public class UserDTO {

	private String userId;
	private String userPw;
	private String username;
	private String nickname;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate birth;
	private Role roles;
}
