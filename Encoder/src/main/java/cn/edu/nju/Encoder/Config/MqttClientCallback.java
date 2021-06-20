package cn.edu.nju.Encoder.Config;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import cn.edu.nju.Encoder.Util.MinIOUtil;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;

public class MqttClientCallback implements MqttCallback {

    private MinIOUtil minIOUtil;
    private IVCompressor compressor = new IVCompressor();

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        byte[] b = message.getPayload();
        String result = "";
        try {
            result = new String(b, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        byte[] bytes = minIOUtil.download(result);
        System.out.println(bytes.length);
        byte[] bytes1080 = null;
        byte[] bytes720 = null;
        byte[] bytes360 = null;
        try {
            bytes1080 = compressor.reduceVideoSize(bytes, VideoFormats.MP4, ResizeResolution.R1080P);
            bytes720 = compressor.reduceVideoSize(bytes, VideoFormats.MP4, ResizeResolution.R720P);
            bytes360 = compressor.reduceVideoSize(bytes, VideoFormats.MP4, ResizeResolution.R360P);
        } catch (VideoException e) {
            e.printStackTrace();
        }
        minIOUtil.upload(bytes1080, result, "-1080P");
        minIOUtil.upload(bytes720, result, "-720P");
        minIOUtil.upload(bytes360, result, "-360P");
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connectionLost:" + cause.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete:" + token.toString());
    }

    public MqttClientCallback(MinIOUtil minIOUtil) {
        this.minIOUtil = minIOUtil;
    }
}