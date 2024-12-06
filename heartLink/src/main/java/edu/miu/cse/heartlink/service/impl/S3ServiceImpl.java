package edu.miu.cse.heartlink.service.impl;

import edu.miu.cse.heartlink.config.awsconfig.S3PresignerExample;
import edu.miu.cse.heartlink.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    //test upload image seperate API
    public String uploadImage(String bucketName, String key, byte[] imageBytes, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(imageBytes));

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toString();
    }

    @Override
    public String generatePresignedUrl(String bucketName, String key) {
        S3PresignerExample presigner = new S3PresignerExample();
        S3Presigner presigner1 = presigner.createPresigner(); // Use the method above


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        return presigner1.presignPutObject(presignRequest).url().toString();
    }


    public void deleteImage(String bucketName, String key) {
        try {
            // Create a DeleteObjectRequest
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Call S3 to delete the object
            s3Client.deleteObject(deleteObjectRequest);
            System.out.println("Image deleted successfully from S3: " + key);

        } catch (NoSuchKeyException e) {
            System.err.println("Image not found in S3 (NoSuchKey): " + key);
            // Optional: log or decide whether to rethrow the exception
        } catch (S3Exception e) {
            System.err.println("Failed to delete image from S3: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to delete image from S3", e);
        } catch (Exception e) {
            System.err.println("Unexpected error occurred while deleting image: " + e.getMessage());
            throw new RuntimeException("Unexpected error occurred while deleting image", e);
        }
    }

}
