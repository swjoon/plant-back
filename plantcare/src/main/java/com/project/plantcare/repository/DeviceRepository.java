package com.project.plantcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.plantcare.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>{

}