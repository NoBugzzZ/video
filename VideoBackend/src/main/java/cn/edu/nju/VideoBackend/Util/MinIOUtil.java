package cn.edu.nju.VideoBackend.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;

@Component
public class MinIOUtil {
    private final static Logger logger = LoggerFactory.getLogger(MinIOUtil.class);

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private ObjectConsumer objectConsumer;

    @Value("${minio.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
            return upload(byteArrayInputStream, file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String upload(InputStream inputStream, String fileName) {

        String name = fileName.substring(0, fileName.lastIndexOf(".mp4"));
        String suffix = ".mp4";
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder objectName = new StringBuilder("[" + localDateTime.format(dateTimeFormatter) + "-");
        objectName.append(System.currentTimeMillis()).append("]").append(name).append("-origin").append(suffix);
        System.out.println(objectName.toString());
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName.toString())
                    .stream(inputStream, -1, 10485760).build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            logger.error("添加失败", e);
        }
        return objectName.toString();
    }

    public String listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
        objectConsumer.clear();
        results.forEach(objectConsumer);
        return objectConsumer.getObjectsName();
    }

    public byte[] download(String fileName) {

        // System.out.println(fileName);
        byte[] bytes = null;
        InputStream inputStream = null;
        try {
            // StatObjectResponse statObjectResponse = minioClient
            // .statObject(StatObjectArgs.builder().bucket(bucket).object(fileName).build());
            // System.out.println(statObjectResponse);

            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build());
            bytes = IOUtils.toByteArray(inputStream);
            ;
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException |

                IOException e) {
            logger.error("下载失败", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

}