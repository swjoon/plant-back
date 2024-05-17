package com.project.plantcare.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDTO {
	private String deviceId;
	private double ledV;
	private double tempV;
	private double humidityV;
	private double shumidityV;
	private Timestamp timestamp;
}
