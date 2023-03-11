package com.saha.amit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.FileNotFoundException;
import java.io.IOException;


@SpringBootApplication
public class S3Application {
    public static void main(String[] args) {
        SpringApplication.run(S3Application.class,args);

        ProfileCredentialsProvider profileCredentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.AP_SOUTH_1;
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(profileCredentialsProvider)
                .build();

        String bucketName = "dev-amit-test-bucket";
        S3ListBucket.listBucket(s3Client);
        String fileNme = "CA1_wallpaper";
        try {
            S3UploadFile.uploadFile(s3Client, bucketName, fileNme);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            S3GetObject.getObject(s3Client, bucketName, fileNme);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            S3MultiPartUpload.multipartUpload(s3Client,bucketName, "multiPartKey");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
