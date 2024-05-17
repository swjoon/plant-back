package com.project.plantcare.entity;

import java.time.LocalDateTime;

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
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;

	@Column(name = "board_no")
	private Long boardNo;

	private String content;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "nickname")
	private String nickName;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Builder
	public Comment(Long no, Long boardNo, String content, String userId, String nickName, LocalDateTime createdDate) {
		super();
		this.no = no;
		this.boardNo = boardNo;
		this.content = content;
		this.userId = userId;
		this.nickName = nickName;
		this.createdDate = LocalDateTime.now();
	}

}