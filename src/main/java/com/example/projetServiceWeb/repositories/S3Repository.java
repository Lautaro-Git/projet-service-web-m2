package com.example.projetServiceWeb.repositories;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;

@Repository
public class S3Repository {
    private final String s3AccessKeyId = "cqBlF80IVJrc1isWW6oK";
    private final String s3SecretKey = "KPJlY826Qi0d952w4GWmQIvA0zfvWi62hvIyiZlT";
    private final String s3url = "https://s3.bober.ovh";

    public String getFileUrl(String bucket, String key) {
        try (var s3Presigner = S3Presigner.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.builder()
                        .accessKeyId(s3AccessKeyId)
                        .secretAccessKey(s3SecretKey)
                        .build()))
                .endpointOverride(URI.create(s3url))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build()) {

            GetObjectPresignRequest request = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build())
                    .build();

            return s3Presigner.presignGetObject(request)
                    .url()
                    .toExternalForm();
        }
    }

    public String getUploadFileUrl(String bucket, String key) {
        try (var s3Presigner = S3Presigner.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.builder()
                        .accessKeyId(s3AccessKeyId)
                        .secretAccessKey(s3SecretKey)
                        .build()))
                .endpointOverride(URI.create(s3url))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build()) {

            PutObjectPresignRequest request = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build())
                    .build();

            return s3Presigner.presignPutObject(request)
                    .url()
                    .toExternalForm();
        }
    }
}
