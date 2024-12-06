package edu.miu.cse.heartlink.config.awsconfig;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

public class S3PresignerExample {

    @Value("${aws.s3.access-key}")
    public String awsAccessKey;

    @Value("${aws.s3.secret-access-key}")
    public String awsSecretKey;

    public S3Presigner createPresigner() {

        return S3Presigner.builder()
                .region(Region.US_EAST_1) // Replace with your region
                .credentialsProvider(StaticCredentialsProvider.create(

                        AwsBasicCredentials.create(awsAccessKey, awsSecretKey)
                ))
                .build();
    }
}
