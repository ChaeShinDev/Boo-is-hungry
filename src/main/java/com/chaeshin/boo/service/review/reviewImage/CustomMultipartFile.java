package com.chaeshin.boo.service.review.reviewImage;
import java.nio.file.Files;
import org.springframework.lang.NonNullApi;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

/**
 * File -> MultipartFile 타입으로 가공하는 클래스. MultipartFile의 구현체.
 */

public class CustomMultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private final String fileName;
    private final String contentType;
    private final long size;

    public CustomMultipartFile(File file) throws IOException {
        this.fileName = file.getName();
        this.size = file.length();
        this.contentType = Files.probeContentType(file.toPath());
        this.fileContent = Files.readAllBytes(file.toPath());
    }

    @Override
    public String getName() {
        // Return the parameter name (similar to how it would be in an HTML form)
        return "image";
    }

    @Override
    public String getOriginalFilename() {
        // Return the original file name as uploaded by the user
        return this.fileName;
    }

    @Override
    public String getContentType() {
        // Return the content type of the file
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        // Return whether the file is empty
        return this.size == 0;
    }

    @Override
    public long getSize() {
        // Return the size of the file in bytes
        return this.size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        // Return the file's data
        return this.fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        // Return an InputStream to read the file's data
        return new ByteArrayInputStream(this.fileContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        // Transfer the received file to the given destination file
        try (OutputStream outputStream = new FileOutputStream(dest)) {
            outputStream.write(this.fileContent);
        }
    }
}
