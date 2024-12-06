package edu.miu.cse.heartlink.config.awsconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    @Value("${aws.s3.access-key}")
    public String awsAccessKey;

    @Value("${aws.s3.secret-access-key}")
    public String awsSecretKey;

    @Bean
    public S3Client s3Client(){
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
       //AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create("AKIA4LV5BQSPUQU5KPY3", "xJ/fd3FQt5ZNsuerrPdURek/CqAprxAJAu1wuJj+");
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}
