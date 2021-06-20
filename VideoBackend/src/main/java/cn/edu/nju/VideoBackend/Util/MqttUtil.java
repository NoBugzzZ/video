package cn.edu.nju.VideoBackend.Util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttUtil {
    private final static Logger logger = LoggerFactory.getLogger(MinIOUtil.class);

    @Autowired
    private MqttClient mqttClient;

    @Value("${mqtt.topic}")
    private String topic;

    private boolean isConnected = false;

    public void connect() {
        if (!isConnected) {
            try {
                mqttClient.connect();
            } catch (MqttException e) {
                logger.error("连接失败", e);
            }
        }

    }

    public void publish(byte[] bytes) {
        MqttMessage message = new MqttMessage();
        message.setPayload(bytes);
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}