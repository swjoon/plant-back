package com.project.plantcare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.plantcare.config.MqttConfig;

@Service
public class MqttService {

	private final MqttConfig.MqttGateway mqttGateway;

	@Autowired
	public MqttService(MqttConfig.MqttGateway mqttGateway) {
		this.mqttGateway = mqttGateway;
	}

	public void sendMessage(String topic, String message) {
		System.out.println("전송시도");
		mqttGateway.sendToMqtt(message, topic);
	}
}
