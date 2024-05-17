package com.project.plantcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	
	@Query("SELECT b FROM Board b ORDER BY b.createdDate DESC")
	List<Board> getBoardList();

}
