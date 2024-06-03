package com.project.plantcare.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;


@Configuration
public class MqttConfig {

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] { "tcp://3.34.190.173:1883" }); // 설정
//		options.setServerURIs(new String[] { "tcp://localhost:1883" }); // 설정
        options.setCleanSession(true); 
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("springClient", mqttClientFactory());
		messageHandler.setAsync(true);
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MqttGateway {
		void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
	}

}
