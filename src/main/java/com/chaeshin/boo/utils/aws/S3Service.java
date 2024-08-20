package com.chaeshin.boo.utils.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    public static final String FILE_EXTENSION_SEPARATOR = ".";
    private final S3Client s3Client;
    private static final String SUFFIX = ".jpg";

    public String uploadFile(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            log.info("file is null");
            return "";
        }

        String fileName = getFileName(image);
        File resizedImage = resizeImage(image, fileName);
        String fileKey = fileName + SUFFIX;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .contentType("image/jpg")
                    .key(fileKey).build();
            RequestBody requestBody = RequestBody.fromFile(resizedImage);
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (Exception e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException(e);
        }
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket).key(fileKey).build();
        return s3Client.utilities().getUrl(getUrlRequest).toString();

    }


    private static String getFileName(MultipartFile file) {
        if(file.isEmpty()) {return "";}
        return buildFileName(file.getOriginalFilename());
    }


    private static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR); // 파일 확장자 구분점
        String fileName = originalFileName.substring(0, fileExtensionIndex); // 파일 이름
        String now = String.valueOf(System.currentTimeMillis()); // 파일 업로드 시간
        return fileName + now;
    }


    private static File resizeImage(MultipartFile image, String fileName) throws IOException {
        try {
            File file = File.createTempFile(fileName, SUFFIX);

            Thumbnails.of(image.getInputStream())
                    .size(800, 600)
                    .outputQuality(0.7)
                    .outputFormat("jpg")
                    .toFile(file);
            return file;
        } catch (IOException e) {
            throw new IOException("이미지 압축 실패", e);
        }
    }
}