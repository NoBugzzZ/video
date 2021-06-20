package cn.edu.nju.VideoBackend.Config;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Configuration
public class MinIOConfig {
    private final static Logger logger = LoggerFactory.getLogger(MinIOConfig.class);
    @Value("${minio.Endpoint}")
    private String endpoint;
    @Value("${minio.RootUser}")
    private String rootUser;
    @Value("${minio.RootPass}")
    private String rootPass;
    @Value("${minio.bucket}")
    private String bucket;

    @Override
    public String toString() {
        return "MinIOConfig [bucket=" + bucket + ", endpoint=" + endpoint + ", rootPass=" + rootPass + ", rootUser="
                + rootUser + "]";
    }

    @Bean(name = "minioClient")
    public MinioClient minioClient() {
        logger.info("正在初始化MinIO客户端");
        MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(rootUser, rootPass).build();
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                logger.info("MinIO不存在bucket " + bucket + ",将创建该bucket");
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            } else {
                logger.info("MinIO已存在bucket " + bucket);
            }
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException e) {
            logger.error("MinIO初始化异常", e);
        }
        logger.info("MinIO初始化完成");
        return minioClient;
    }
}