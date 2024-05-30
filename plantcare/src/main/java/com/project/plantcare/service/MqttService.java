package com.project.plantcare.service;

import org.springframework.stereotype.Service;

import com.project.plantcare.config.MqttConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MqttService {
	private MqttConfig.MqttGateway mqttGateway;
	
	public void sendMessage(String topic, String message) {
		mqttGateway.sendToMqtt(message, topic);
	}
}
