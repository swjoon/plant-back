package com.project.plantcare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.plantcare.config.SecurityUtil;
import com.project.plantcare.dto.DeviceDTO;
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
	
	@GetMapping("/getdevicedetail")
	public ResponseEntity<?> getDeviceDetail(@RequestBody String deviceId){
		
		
		
		
		
		return ResponseEntity.ok(null);
	}
	
	

}
