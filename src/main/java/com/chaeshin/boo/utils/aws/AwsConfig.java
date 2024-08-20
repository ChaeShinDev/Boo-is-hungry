package com.chaeshin.boo.utils.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
    static private String accessKey;
    static private String secretKey;
    static private String region;


    public AwsConfig(@Value("${spring.cloud.aws.credentials.accessKey}") String accessKey,
                     @Value("${spring.cloud.aws.credentials.secretKey}") String secretKey,
                     @Value("${spring.cloud.aws.region.static}") String region) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(this::awsCredentials)
                .region(Region.of(region))
                .build();
    }

    private AwsCredentials awsCredentials() {
        return new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return accessKey;
            }

            @Override
            public String secretAccessKey() {
                return secretKey;
            }
        };
    }
}
