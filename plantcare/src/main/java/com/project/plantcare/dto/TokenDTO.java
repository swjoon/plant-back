package com.project.plantcare.dto;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
	
	private String tokenType;
	private String accessToken;
	private String refreshToken;
	private Duration duration;
}
