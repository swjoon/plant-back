package com.project.plantcare.dto;

import com.project.plantcare.entity.Device;
import com.project.plantcare.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeviceDTO {

	private User user;
	private Device device;
	private String deviceName;

}
