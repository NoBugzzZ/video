package cn.edu.nju.Encoder.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

public class MinIOUtil {
    private final static Logger logger = LoggerFactory.getLogger(MinIOUtil.class);

    private MinioClient minioClient;

    private String bucket;

    public void upload(byte[] bytes, String fileName, String suffix) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        String newName = fileName.replace("-origin", suffix);
        System.out.println(newName + suffix + " " + bucket);
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(newName)
                    .stream(byteArrayInputStream, -1, 10485760).build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            logger.error("添加失败", e);
        }
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

    public MinIOUtil(MinioClient minioClient, String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

}