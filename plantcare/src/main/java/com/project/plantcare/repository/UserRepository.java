package com.project.plantcare.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	   Optional<User> findByUserId(String userId);
	
}
