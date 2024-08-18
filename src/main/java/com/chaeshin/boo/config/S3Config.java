package com.chaeshin.boo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;


    /**
     * S3Client<p></p>
     * - <i>"Interface that provides a 'low-level API' for interacting with AWS S3 Services."</i><br></br>
     * - <i>"... Allows us directly <b>call methods to create buckets, upload objects, download objects, delete objects</b> ..."</i><br></br>
     *
     * @return
     */
    @Bean
    public S3Client s3Client() {
        // AWSBasicCredentials : Args-Constructor 의 접근자가 protected 임. -> 직접 생성 제한
        //                       따라서 해당 소스에 별도로 정의된 생성 메서드를 활용할 것.
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

    }
}
