package com.project.plantcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.User;
import com.project.plantcare.entity.UserDevice;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer >{

	Optional<List<UserDevice>> findByUser(User user);

	UserDevice findByDevice(Device device);
	
	void deleteByDevice(Device device);
}
