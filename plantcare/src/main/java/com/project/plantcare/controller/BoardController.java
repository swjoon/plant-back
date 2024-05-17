package com.project.plantcare.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.plantcare.config.SecurityUtil;
import com.project.plantcare.dto.BoardDTO;
import com.project.plantcare.dto.BoardListDTO;
import com.project.plantcare.dto.CommentDTO;
import com.project.plantcare.dto.CommentListDTO;
import com.project.plantcare.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

	private final BoardService boardService;
	
	@GetMapping("/")
	public ResponseEntity<List<BoardListDTO>> getBoardList() {
		List<BoardListDTO> boardList = boardService.getBoardList();
		System.out.println(Arrays.deepToString(boardList.toArray()));
		return ResponseEntity.ok(boardList);
	}
	
	@PostMapping("/post")
	public void setBoard(@RequestBody BoardDTO boardDTO) {		
		String currentUsername = SecurityUtil.getCurrentUsername();
		boardService.postBoard(currentUsername, boardDTO);
	}
	
	@PutMapping("/post")
	public void updateBoard(@RequestParam Long No, @RequestBody BoardDTO boardDTO ) {
		boardService.updateBoard(No, boardDTO);
	}
	
	@PostMapping("/delete")
	public void deleteBoard(@RequestBody Map<String, Long> requestBody) {
		Long no = requestBody.get("no");
	    boardService.deleteBoard(no, SecurityUtil.getCurrentUsername(), SecurityUtil.getCurrentUserRole());
	}
	
	@GetMapping("/detail")
	public ResponseEntity<BoardDTO> getBoardDetail(@RequestParam Long no){
		BoardDTO boardDTO = boardService.getBoardDetail(no);
		return ResponseEntity.ok(boardDTO);
	}
	
	@PostMapping("/comment")
	public void setComment(@RequestBody CommentDTO commentDTO) {
		try {
			String currentUsername = SecurityUtil.getCurrentUsername();
			boardService.postComment(currentUsername,commentDTO);
		} catch (NotFoundException e) {

			e.printStackTrace();
		}
	}
	
	@GetMapping("/comment")
	public ResponseEntity<List<CommentListDTO>> getComment(@RequestParam Long no) {
		List<CommentListDTO> commentList = boardService.getCommentList(no);
		return ResponseEntity.ok(commentList);
	}
	
	@PostMapping("/comment/delete")
	public void deleteComment(@RequestBody Map<String, Long> requestBody) {
	    Long no = requestBody.get("no");
	    boardService.deleteComment(no, SecurityUtil.getCurrentUsername(), SecurityUtil.getCurrentUserRole());
	}
	
}
