package com.project.plantcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.plantcare.config.MqttConfig;

@Service
public class MqttService {
	
	@Autowired
	private MqttConfig.MqttGateway mqttGateway;
    
	public void sendMessage(String topic, String message) {
		mqttGateway.sendToMqtt(message, topic);
	}
}
