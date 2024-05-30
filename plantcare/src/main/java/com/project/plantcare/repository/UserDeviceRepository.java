package com.project.plantcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.User;
import com.project.plantcare.entity.UserDevice;

import jakarta.transaction.Transactional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer >{

	Optional<List<UserDevice>> findByUser(User user);

	UserDevice findByDevice(Device device);
	
	void deleteByDevice(Device device);
	
}
