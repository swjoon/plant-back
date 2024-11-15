package com.project.plantcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.plantcare.dto.SensorDataDTO;
import com.project.plantcare.dto.SetDataDTO;
import com.project.plantcare.service.DeviceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sensor")
public class SensorController {
	
	private final DeviceService deviceService;
	
	@PostMapping("/postnowdata")
	public ResponseEntity<?> saveNowData(@RequestBody SensorDataDTO sensorDataDTO){
		try {
			deviceService.saveSensorData(sensorDataDTO);
			return ResponseEntity.ok(200);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid token");
		}
	}
	
	@GetMapping("/nowdata")
	public ResponseEntity<?> getNowData(@RequestParam String deviceId){
		try {
			SensorDataDTO sensorDataDTO = deviceService.getlastestdata(deviceId);
			return ResponseEntity.ok(sensorDataDTO);			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid token");
		}
	}
	
	@GetMapping("/getsetdata")
	public ResponseEntity<?> getData(@RequestParam String deviceId){
		SetDataDTO setDataDTO = deviceService.getSetData(deviceId);
		return ResponseEntity.ok(setDataDTO);
	}
	
	@GetMapping("/getled")
	public ResponseEntity<?> getLed(@RequestParam String deviceId){
		SetDataDTO setDataDTO = deviceService.getSetData(deviceId);
		int led = setDataDTO.getLedV();
		return ResponseEntity.ok(led);
	}
}
