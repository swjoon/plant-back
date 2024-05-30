package com.project.plantcare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.plantcare.config.MqttConfig;
import com.project.plantcare.dto.DeviceDTO;
import com.project.plantcare.dto.LedDTO;
import com.project.plantcare.dto.SensorDataDTO;
import com.project.plantcare.dto.SetDataDTO;
import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.SensorData;
import com.project.plantcare.entity.SetData;
import com.project.plantcare.entity.User;
import com.project.plantcare.entity.UserDevice;
import com.project.plantcare.repository.DeviceRepository;
import com.project.plantcare.repository.SensorDataRepository;
import com.project.plantcare.repository.SetDataRepository;
import com.project.plantcare.repository.UserDeviceRepository;
import com.project.plantcare.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {

	private final DeviceRepository deviceRepository;
	private final UserRepository userRepository;
	private final UserDeviceRepository userDeviceRepository;
	private final SetDataRepository setDataRepository;
	private final SensorDataRepository sensorDataRepository;
	private MqttConfig.MqttGateway mqttGateway;

	// device 중복체크 및 사용 등록
	public boolean deviceCheck(DeviceDTO deviceDTO) {
		Optional<Device> optionalDevice = deviceRepository.findById(deviceDTO.getDeviceId());
		if (optionalDevice.isPresent()) {
			Device device = optionalDevice.get();
			if (device.isDeviceUse()) {
				return false;
			}
			if (device.getDevicePw().equals(deviceDTO.getDevicePw())) {
				device.setDeviceUse(true);
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	// device 유저 테이블에 저장
	public void save(String userId, DeviceDTO deviceDTO) {
		Optional<User> user = userRepository.findByUserId(userId);
		Optional<Device> device = deviceRepository.findById(deviceDTO.getDeviceId());
		UserDevice userDevice = UserDevice.builder().user(user.get()).device(device.get()).deviceName("새싹").build();
		SetData setData = SetData.builder().device(device.get()).ledV(0).tempV(25).humidityV(50).shumidityV(50).build();
		setDataRepository.save(setData);
		userDeviceRepository.save(userDevice);

	}

	@Transactional
	public void delete(String userId, String deviceId) {
		Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
		Device device = optionalDevice.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		device.setDeviceUse(false);
		deviceRepository.save(device);
		sensorDataRepository.deleteByDevice(device);
		setDataRepository.deleteByDevice(device);
		userDeviceRepository.deleteByDevice(device);
	}

	// device 목록 불러오기
	public List<UserDevice> getUserDevice(String userId) {

		Optional<User> user = userRepository.findById(userId);
		Optional<List<UserDevice>> optionalUserDevices = userDeviceRepository.findByUser(user.get());

		return optionalUserDevices.orElse(new ArrayList<>());
	}

	// device당 setdata 목록 불러오기.
	public SetDataDTO getSetData(String deviceId) {
		Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
		Device device = optionalDevice.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SetData setdata = setDataRepository.findByDevice(device);
		UserDevice userDevice = userDeviceRepository.findByDevice(device);
		SetDataDTO setDataDTO = SetDataDTO.builder().deviceName(userDevice.getDeviceName()).ledV(setdata.getLedV())
				.tempV(setdata.getTempV()).humidityV(setdata.getHumidityV()).shumidityV(setdata.getShumidityV())
				.build();

		return setDataDTO;
	}

	// 임계값 넘어오면 수정후 업데이트.
	public void updateData(SetDataDTO setDataDTO) {
		Optional<Device> optionalDevice = deviceRepository.findById(setDataDTO.getDeviceId());
		Device device = optionalDevice.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SetData setData = setDataRepository.findByDevice(device);
		UserDevice userDevice = userDeviceRepository.findByDevice(device);
		setData.setTempV(setDataDTO.getTempV());
		setData.setHumidityV(setDataDTO.getHumidityV());
		setData.setShumidityV(setDataDTO.getShumidityV());
		userDevice.setUserDeviceName(setDataDTO.getDeviceName());
		setDataRepository.save(setData);
		userDeviceRepository.save(userDevice);
		// mqtt로 디바이스에게 신호 전달
		String topic = "device/" + setDataDTO.getDeviceId() + "/setdata";
		mqttGateway.sendToMqtt(setDataDTO.toString(), topic);
	}

	// 조명 on/off
	public void ledChange(LedDTO ledDTO) {
		Optional<Device> optionalDevice = deviceRepository.findById(ledDTO.getDeviceId());
		Device device = optionalDevice.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SetData setData = setDataRepository.findByDevice(device);
		System.out.println("현재 조명 상태: " + ledDTO.getLedV());
		setData.setLedV(ledDTO.getLedV());
		setDataRepository.save(setData);
	}

	// 센서값들 저장
	public void saveSensorData(SensorDataDTO sensorDataDTO) {
		Optional<Device> optionalDevice = deviceRepository.findById(sensorDataDTO.getDeviceId());
		if (optionalDevice.isPresent()) {
			Device device = optionalDevice.get();
			System.out.println(
					sensorDataDTO.getLedV() + " " + sensorDataDTO.getTempV() + " " + sensorDataDTO.getHumidityV() + " "
							+ sensorDataDTO.getShumidityV() + " " + sensorDataDTO.getTimestamp());
			SensorData sensorData = SensorData.builder().device(device).ledV(sensorDataDTO.getLedV())
					.tempV(sensorDataDTO.getTempV()).humidityV(sensorDataDTO.getHumidityV())
					.shumidityV(sensorDataDTO.getShumidityV()).timestamp(sensorDataDTO.getTimestamp()).build();
			sensorDataRepository.save(sensorData);
		} else {
			throw new RuntimeException("Device not found");
		}
	}

	// 최근 센서값 불러오기
	public SensorDataDTO getlastestdata(String deviceId) {
		Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
		if (optionalDevice.isPresent()) {
			Device device = optionalDevice.get();
			SensorData sensorData = sensorDataRepository.findLastestData(device);
			if (sensorData != null) {
				return SensorDataDTO.builder().ledV(sensorData.getLedV()).tempV(sensorData.getTempV())
						.humidityV(sensorData.getHumidityV()).shumidityV(sensorData.getShumidityV())
						.timestamp(sensorData.getTimestamp()).build();
			} else {
				return SensorDataDTO.builder().ledV(0).tempV(0).humidityV(0).shumidityV(0).build();
			}
		} else {
			throw new RuntimeException("Device not found: " + deviceId);
		}
	}

}
