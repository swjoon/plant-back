package com.project.plantcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.SensorData;

public interface SensorDataRepository extends JpaRepository<SensorData, Integer>{
	@Query("SELECT sd FROM SensorData sd WHERE sd.device = :device ORDER BY sd.dataId DESC LIMIT 1")
	SensorData findLastestData(Device device);
	
	void deleteByDevice(Device device);
}
