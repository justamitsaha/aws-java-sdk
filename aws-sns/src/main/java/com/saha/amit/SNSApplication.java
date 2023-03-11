package com.saha.amit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@SpringBootApplication
public class SNSApplication {

    public static void main(String[] args) {
        SpringApplication.run(SNSApplication.class,args);

        ProfileCredentialsProvider profileCredentialsProvider = ProfileCredentialsProvider.create();
        SnsClient snsClient = SnsClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        String arnVal = createSNSTopic(snsClient, "chul") ;
        System.out.println("The topic ARN is" +arnVal);
        snsClient.close();

    }

    public static String createSNSTopic(SnsClient snsClient, String topicName ) {

        CreateTopicResponse result = null;
        try {
            CreateTopicRequest request = CreateTopicRequest.builder()
                    .name(topicName)
                    .build();

            result = snsClient.createTopic(request);
            return result.topicArn();

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }


}
