package com.project.plantcare.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.plantcare.config.SecurityUtil;
import com.project.plantcare.dto.DeviceDTO;
import com.project.plantcare.dto.LedDTO;
import com.project.plantcare.dto.SetDataDTO;
import com.project.plantcare.entity.User;
import com.project.plantcare.entity.UserDevice;
import com.project.plantcare.service.DeviceService;
import com.project.plantcare.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainController {

	private final DeviceService deviceService;
	private final UserService userService;

	@GetMapping("/home")
	public ResponseEntity<?> getuserdata() {
		try {
			Optional<User> user = userService.getUser(SecurityUtil.getCurrentUsername());
			return ResponseEntity.ok(user.orElse(null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid token");
		}
	}

	@PostMapping("/addDevice")
	public ResponseEntity<?> addDevice(@RequestBody DeviceDTO deviceDTO) {
		try {
			if (deviceService.deviceCheck(deviceDTO)) {
				String currentUsername = SecurityUtil.getCurrentUsername();
				deviceService.save(currentUsername, deviceDTO);
				return ResponseEntity.ok(200);
			} else
				return ResponseEntity.badRequest().body("Device 등록 실패");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid token");
		}
	}
	
	@GetMapping("/getDevice")
	public ResponseEntity<List<UserDevice>> getDevice(){
		String userId = SecurityUtil.getCurrentUsername();
		List<UserDevice> deviceList = deviceService.getUserDevice(userId);	
		if(deviceList.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(deviceList);		
	}
	
	@PostMapping("/device")
	public void deleteDevice(@RequestBody Map<String, String> requestBody) {
		String userId = SecurityUtil.getCurrentUsername();
		String deviceId = requestBody.get("deviceId");
		deviceService.delete(userId, deviceId);
	}
	
	@GetMapping("/getdevicedetail")
	public ResponseEntity<?> getDeviceDetail(@RequestParam String deviceId){
		try{
			System.out.println("요기?");
			SetDataDTO setDataDTO = deviceService.getSetData(deviceId);
			return ResponseEntity.ok(setDataDTO);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid token");
		}
	}
	
	@PostMapping("/settingdata")
	public ResponseEntity<?> updateSensorData(@RequestBody SetDataDTO setDataDTO){
		try {
			deviceService.updateData(setDataDTO);	
			return ResponseEntity.ok(200);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid token");
		}
	}
	
	@PostMapping("/ledV")
	public ResponseEntity<?> updateLedData(@RequestBody LedDTO ledDTO){
		try {
			deviceService.ledChange(ledDTO);
			return ResponseEntity.ok(200);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid token");
		}
	}

}
