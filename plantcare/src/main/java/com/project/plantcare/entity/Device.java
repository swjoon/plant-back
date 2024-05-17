package com.project.plantcare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Device")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {
	@Id
	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "device_pw")
	private String devicePw;

	@Column(name = "device_use")
	private boolean deviceUse;

	public boolean isDeviceUse() {
		return deviceUse;
	}

	public void setDeviceUse(boolean deviceUse) {
		this.deviceUse = deviceUse;
	}

}
