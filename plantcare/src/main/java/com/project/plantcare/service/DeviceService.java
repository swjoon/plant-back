package com.project.plantcare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.plantcare.dto.DeviceDTO;
import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.User;
import com.project.plantcare.entity.UserDevice;
import com.project.plantcare.repository.DeviceRepository;
import com.project.plantcare.repository.UserDeviceRepository;
import com.project.plantcare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {

	private final DeviceRepository deviceRepository;
	private final UserRepository userRepository;
	private final UserDeviceRepository userDeviceRepository;
	
	// device 중복체크 및 사용 등록
	public boolean deviceCheck(DeviceDTO deviceDTO) {
		Optional<Device> optionalDevice = deviceRepository.findById(deviceDTO.getDeviceId());
			if(optionalDevice.isPresent()) {
				  Device device = optionalDevice.get();
				  if(device.isDeviceUse()) {
					  return false;
				  }
				  if(device.getDevicePw().equals(deviceDTO.getDevicePw())) {
					  device.setDeviceUse(true);		
					  return true;
				  }
				  return false;
			}else {
				return false;
			}
		}
	
	// device 유저 테이블에 저장
	public void save(String userId, DeviceDTO deviceDTO) {
		
		Optional<User> user = userRepository.findByUserId(userId);
		Optional<Device> device = deviceRepository.findById(deviceDTO.getDeviceId());
		UserDevice userDevice = UserDevice.builder().user(user.get()).device(device.get()).deviceName("새싹").build();
		
		userDeviceRepository.save(userDevice);
	}
	
	// device 목록 불러오기
	public List<UserDevice> getUserDevice(String userId){
		
		Optional<User> user = userRepository.findById(userId);
		Optional<List<UserDevice>> optionalUserDevices = userDeviceRepository.findByUser(user.get());

	    return optionalUserDevices.orElse(new ArrayList<>());
	}

}
