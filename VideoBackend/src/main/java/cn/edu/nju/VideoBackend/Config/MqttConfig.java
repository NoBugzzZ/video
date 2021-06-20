package cn.edu.nju.VideoBackend.Config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    @Value("${mqtt.host}")
    private String host;
    @Value("${mqtt.port}")
    private String port;
    @Value("${mqtt.topic}")
    private String topic;

    @Bean(name = "mqttClient")
    public MqttClient mqttClient() {
        try {
            return new MqttClient(host + ":" + port, MqttClient.generateClientId());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MqttConfig [host=" + host + ", port=" + port + ", topic=" + topic + "]";
    }
}