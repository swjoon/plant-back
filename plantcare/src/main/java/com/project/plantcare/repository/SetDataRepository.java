package com.project.plantcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.SetData;

@Repository
public interface SetDataRepository extends JpaRepository<SetData, Integer> {

	SetData findByDevice(Device device);

	void deleteByDevice(Device device);
}
