package cn.edu.nju.Encoder.Config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConfig {
    private String host;
    private String port;
    private String topic;

    @Override
    public String toString() {
        return "MqttConfig [host=" + host + ", port=" + port + ", topic=" + topic + "]";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttConfig(String host, String port, String topic) {
        this.host = host;
        this.port = port;
        this.topic = topic;
    }

    public MqttClient mqttClient() {
        try {
            return new MqttClient(host + ":" + port, MqttClient.generateClientId());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }
}