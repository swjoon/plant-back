package com.project.plantcare.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {

	private String userId;
	private String username;
	private String nickname;
	private LocalDate birth;
	private LocalDateTime createdDate;
	
}
