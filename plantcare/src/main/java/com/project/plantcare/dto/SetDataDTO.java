package com.project.plantcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetDataDTO {
	private String deviceId;
	private String deviceName;
	private int ledV;
    private int tempV;
    private int humidityV;
    private int shumidityV;
}
