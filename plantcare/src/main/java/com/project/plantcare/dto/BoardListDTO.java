package com.project.plantcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListDTO {
	
	private Long no;
    private String title;
    private String nickName;
    private String createdDate;
    private int commentCount;
    
}
