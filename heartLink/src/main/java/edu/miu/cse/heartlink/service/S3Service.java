package edu.miu.cse.heartlink.service;

import java.io.InputStream;

public interface S3Service {

    String uploadImage(String bucketName, String key, byte[] imageBytes, String contentType);

    String generatePresignedUrl(String bucketName, String key);

    void deleteImage(String bucketName, String key);
}
