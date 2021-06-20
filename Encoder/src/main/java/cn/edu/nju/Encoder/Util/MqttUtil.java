package cn.edu.nju.Encoder.Util;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttUtil {
    private final static Logger logger = LoggerFactory.getLogger(MinIOUtil.class);

    private MqttClient mqttClient;
    private boolean isConnected = false;

    public MqttUtil(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void connect() {
        if (!isConnected) {
            try {
                mqttClient.connect();
            } catch (MqttException e) {
                logger.error("连接失败", e);
            }
        }

    }

    public void subscribe(MqttCallback callback, String topic) {
        try {
            mqttClient.setCallback(callback);
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}