package com.project.plantcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c WHERE c.boardNo = :no ORDER BY c.createdDate DESC")
	List<Comment> getCommentList(@Param("no") Long no);
	
	@Modifying
	@Query("DELETE FROM Comment c WHERE c.boardNo = :no")
	void deleteByBoardNo(@Param("no") Long no);
}
