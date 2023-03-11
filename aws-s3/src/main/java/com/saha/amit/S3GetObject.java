package com.saha.amit;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.*;

public class S3GetObject {
    public static void getObject (S3Client s3, String bucketName, String fileName) throws IOException {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseInputStream responseInputStream = s3.getObject(getObjectRequest);
        byte[] b= responseInputStream.readAllBytes();
        File file = new File("C:\\Amit\\Work\\intellij_workspace\\aws-java-sdk\\aws-s3\\src\\main\\resources\\CA1_downloaded_s3.jpg");
        try (InputStream in = new ByteArrayInputStream(b);
             var out = new FileOutputStream(file)) {
            int data;
            while ((data = in.read()) != -1) {
                out.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
