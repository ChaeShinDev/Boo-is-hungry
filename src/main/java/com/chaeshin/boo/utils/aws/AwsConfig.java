package com.chaeshin.boo.utils.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {
    static private String accessKey;
    static private String secretKey;
    static private String region;


    public AwsConfig(@Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
                     @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey,
                     @Value("${spring.cloud.aws.s3.region}") String region) {
        AwsConfig.accessKey = accessKey;
        AwsConfig.secretKey = secretKey;
        AwsConfig.region = region;
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
