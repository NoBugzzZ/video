package cn.edu.nju.Encoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import cn.edu.nju.Encoder.Config.MinIOConfig;
import cn.edu.nju.Encoder.Config.MqttClientCallback;
import cn.edu.nju.Encoder.Config.MqttConfig;
import cn.edu.nju.Encoder.Util.MinIOUtil;
import cn.edu.nju.Encoder.Util.MqttUtil;

@SpringBootApplication
public class EncoderApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(EncoderApplication.class, args);
		Environment environment = context.getBean(Environment.class);
		String host = environment.getProperty("mqtt.host");
		String port = environment.getProperty("mqtt.port");
		String topic = environment.getProperty("mqtt.topic");
		MqttConfig mqttConfig = new MqttConfig(host, port, topic);
		System.out.println(mqttConfig.toString());
		MqttUtil mqttUtil = new MqttUtil(mqttConfig.mqttClient());
		mqttUtil.connect();
		String endpoint = environment.getProperty("minio.Endpoint");
		String rootUser = environment.getProperty("minio.RootUser");
		String rootPass = environment.getProperty("minio.RootPass");
		String bucket = environment.getProperty("minio.bucket");
		MinIOConfig minIOConfig = new MinIOConfig(endpoint, rootUser, rootPass, bucket);
		MinIOUtil minIOUtil = new MinIOUtil(minIOConfig.minioClient(), bucket);
		mqttUtil.subscribe(new MqttClientCallback(minIOUtil), topic);
	}

}
