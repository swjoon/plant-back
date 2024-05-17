package com.project.plantcare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="User_Device")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDevice {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_device_id")
    private int userDeviceId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private Device device;

    @Column(name = "device_name")
    private String deviceName;

	public void setUserDeviceId(int userDeviceId) {
		this.userDeviceId = userDeviceId;
	}
}
