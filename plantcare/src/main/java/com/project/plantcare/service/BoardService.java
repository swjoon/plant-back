package com.project.plantcare.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.plantcare.dto.BoardDTO;
import com.project.plantcare.dto.BoardListDTO;
import com.project.plantcare.dto.CommentDTO;
import com.project.plantcare.dto.CommentListDTO;
import com.project.plantcare.entity.Board;
import com.project.plantcare.entity.Comment;
import com.project.plantcare.entity.User;
import com.project.plantcare.repository.BoardRepository;
import com.project.plantcare.repository.CommentRepository;
import com.project.plantcare.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	@Autowired
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public void postBoard(String userId, BoardDTO boardDTO) {
		Optional<User> user = userRepository.findById(userId);
		String nickname = user.get().getNickname();
		Board board = Board.builder().userId(userId).nickName(nickname).title(boardDTO.getTitle())
				.content(boardDTO.getContent()).commentCnt(0).build();
		boardRepository.save(board);
	}
	
	@Transactional
	public void updateBoard(Long No, BoardDTO boardDTO) {
		Optional<Board> optionalBoard = boardRepository.findById(No);
		Board board = optionalBoard.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		board.setTitle(boardDTO.getTitle());
		board.setContent(boardDTO.getContent());
		board.setUpdatedDate(LocalDateTime.now());
		boardRepository.save(board);
	}
	
	@Transactional
	public void deleteBoard(Long no, String userId, String role) {
		Optional<Board> optionalBoard = boardRepository.findById(no);
		Board board = optionalBoard.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found with no: " + no));
		if (board.getUserId().equals(userId) || role.equals("[ROLE_ADMIN]")) {
			commentRepository.deleteByBoardNo(no);
			boardRepository.deleteById(no);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
	}
	
	public BoardDTO getBoardDetail(Long no) {
		Optional<Board> optionalBoard = boardRepository.findById(no);
		if (optionalBoard.isPresent()) {
			Board board = optionalBoard.get();
			BoardDTO boardDTO = BoardDTO.builder().title(board.getTitle()).content(board.getContent()).build();
			return boardDTO;
		}
		return null;
	}
	

	public List<BoardListDTO> getBoardList() {
		List<Board> lists = boardRepository.getBoardList();
		List<BoardListDTO> dtos = new ArrayList<>();
		for (Board list : lists) {
			dtos.add(BoardListDTO.builder().no(list.getNo()).title(list.getTitle()).nickName(list.getNickName())
					.createdDate(list.getCreatedDate().toString().substring(0, 10)).commentCount(list.getCommentCnt())
					.build());
		}
		return dtos;
	}
	
	@Transactional
	public void postComment(String userId, CommentDTO commentDTO) throws NotFoundException {
		Optional<User> optionalUser = userRepository.findById(userId);
		User user = optionalUser.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
		String nickname = user.getNickname();
		Comment comment = Comment.builder().boardNo(commentDTO.getBoardNo()).content(commentDTO.getContent())
				.userId(userId).nickName(nickname).build();
		commentRepository.save(comment);

		Optional<Board> optionalBoard = boardRepository.findById(commentDTO.getBoardNo());
		Board board = optionalBoard.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Board not found with id: " + commentDTO.getBoardNo()));

		board.setCommentCnt(board.getCommentCnt() + 1);
		boardRepository.save(board);

	}

	@Transactional
	public void deleteComment(Long no, String userId, String role) {
		Optional<Comment> optionalComment = commentRepository.findById(no);
		Comment comment = optionalComment.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with no: " + no));
		if (comment.getUserId().equals(userId) || role.equals("[ROLE_ADMIN]")) {
			commentRepository.deleteById(no);
			Optional<Board> optionalBoard = boardRepository.findById(comment.getBoardNo());
			Board board = optionalBoard.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Board not found with id: " + comment.getBoardNo()));

			board.setCommentCnt(board.getCommentCnt() - 1);
			boardRepository.save(board);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
		}
	}

	public List<CommentListDTO> getCommentList(Long no) {
		List<Comment> lists = commentRepository.getCommentList(no);
		List<CommentListDTO> dtos = new ArrayList<>();
		for (Comment list : lists) {
			dtos.add(CommentListDTO.builder().no(list.getNo()).nickName(list.getNickName()).content(list.getContent())
					.createdDate(list.getCreatedDate().toString().substring(5, 7) + "/"
							+ list.getCreatedDate().toString().substring(8, 10) + " "
							+ list.getCreatedDate().toString().substring(11, 16))
					.build());
		}
		return dtos;
	}
}
