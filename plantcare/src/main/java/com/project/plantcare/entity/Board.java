package com.project.plantcare.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Board")
@Getter
@Setter
@NoArgsConstructor
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "nickname")
	private String nickName;

	private String title;

	private String content;
	
	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;
	
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
	
	@Column(name = "comment_count")
	private int commentCnt;

	@Builder
	public Board(Long no, String userId, String nickName, String title, String content, LocalDateTime createdDate, LocalDateTime updatedDate, int commentCnt) {
		this.no = no;
		this.userId = userId;
		this.nickName = nickName;
		this.title = title;
		this.content = content;
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
		this.commentCnt = commentCnt;
	}

}
