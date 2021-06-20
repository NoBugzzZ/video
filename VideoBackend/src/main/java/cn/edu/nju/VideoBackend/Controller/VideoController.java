package cn.edu.nju.VideoBackend.Controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.nju.VideoBackend.Util.MinIOUtil;
import cn.edu.nju.VideoBackend.Util.MqttUtil;

@Controller
public class VideoController {

    @Autowired
    private MinIOUtil minIOUtil;

    @Autowired
    private MqttUtil mqttUtil;

    @PostMapping("/videos")
    ResponseEntity<String> upload(@RequestParam(value = "file", required = true) MultipartFile file) {
        String name = minIOUtil.upload(file);
        mqttUtil.connect();
        mqttUtil.publish(name.getBytes());
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @GetMapping("/videos")
    ResponseEntity<String> listObjects() {
        return new ResponseEntity<>(minIOUtil.listObjects(), HttpStatus.OK);
    }

    @GetMapping("/video")
    ResponseEntity<byte[]> download(@RequestParam(value = "uri", required = true) String uri) {
        String fileName = "name";
        try {
            fileName = java.net.URLDecoder.decode(uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.set("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(minIOUtil.download(fileName), headers, HttpStatus.OK);
    }

}