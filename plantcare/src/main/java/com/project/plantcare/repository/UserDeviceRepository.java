package com.project.plantcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.plantcare.entity.User;
import com.project.plantcare.entity.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer >{

	Optional<List<UserDevice>> findByUser(User user);
	
}
