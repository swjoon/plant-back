package com.project.plantcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {
	private String nickName;
	private String role;
    private String accessToken;
    private String refreshToken;
    private boolean isNewMember;
}