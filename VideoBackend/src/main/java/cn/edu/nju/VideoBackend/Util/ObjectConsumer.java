package cn.edu.nju.VideoBackend.Util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import cn.edu.nju.VideoBackend.model.Video;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;

@Configuration
public class ObjectConsumer implements Consumer<Result<Item>> {

    private final static Logger logger = LoggerFactory.getLogger(ObjectConsumer.class);
    private List<String> objectsName = new ArrayList<String>();
    Gson gson = new Gson();

    @Override
    public void accept(Result<Item> t) {
        try {
            objectsName.add(t.get().objectName());
        } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
                | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                | XmlParserException | IOException e) {
            logger.error("获取对象失败", e);
        }
    }

    public String getObjectsName() {
        List<Video> list = new ArrayList<Video>();
        for (String name : objectsName) {
            Video video = new Video(name);
            list.add(video);
        }
        return gson.toJson(list);
    }

    public void clear() {
        objectsName.clear();
    }

}