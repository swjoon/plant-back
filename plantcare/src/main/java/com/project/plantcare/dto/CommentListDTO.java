package com.project.plantcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentListDTO {
	private Long no;
	private String nickName;
	private String content;
	private String createdDate;
}
