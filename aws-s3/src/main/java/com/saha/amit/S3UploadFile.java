package com.saha.amit;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class S3UploadFile {
    public static void uploadFile(S3Client s3,String bucketName, String fileName) throws FileNotFoundException {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        File file = new File("C:\\Amit\\Work\\intellij_workspace\\aws-java-sdk\\aws-s3\\src\\main\\resources\\CA1.jpg");

        if (file.exists()){
            FileInputStream fs = new FileInputStream(file);
            s3.putObject(objectRequest, RequestBody.fromFile(file));
        }else {
            System.out.println("file doesn't exist");
        }
    }
}
