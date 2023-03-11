import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Scanner;

public class AwsSqsCreateListDeleteQueue {
    public static void main(String[] args) {
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        System.out.print("Please Enter Queue Name to be created --> ");
        Scanner user_input = new Scanner(System.in);
        String queueName = user_input.nextLine();
        System.out.println("Creating queue. " + queueName);
        createQueue(sqsClient, queueName);
        getQueueUrl(sqsClient,queueName);

        System.out.println("Do you want to delete the queue ? Enter y/n");
        user_input = new Scanner(System.in);
        String userInput = user_input.nextLine();
        if (userInput.equalsIgnoreCase("y")) {
            System.out.println("Deleting queue.");
            deleteSQSQueue(sqsClient, queueName);
        } else {
            System.out.println("Queue stay.");
        }

        System.out.println("Do you want to search for a queue ? Enter y/n");
        user_input = new Scanner(System.in);
        userInput = user_input.nextLine();
        if (userInput.equalsIgnoreCase("y")) {
            System.out.println("Enter which queue you want to search --> ");
            user_input = new Scanner(System.in);
            userInput = user_input.nextLine();
            listQueues(sqsClient, userInput);
        } else {
            System.out.println("Listing all queues");
            listQueues(sqsClient,"");
        }

    }

    public static void createQueue(SqsClient sqsClient, String queueName) {
        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName(queueName)
                .build();

        sqsClient.createQueue(createQueueRequest);
        System.out.println("Queue " + queueName + " created.");
    }

    public static void deleteSQSQueue(SqsClient sqsClient, String queueName) {

        try {
            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
            DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build();
            sqsClient.deleteQueue(deleteQueueRequest);
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void getQueueUrl(SqsClient sqsClient, String queueName) {
        try {
            GetQueueUrlResponse getQueueUrlResponse =
                    sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
            String queueUrl = getQueueUrlResponse.queueUrl();
            System.out.println("Queue Url --> "+ queueUrl);

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void listQueues(SqsClient sqsClient, String searchParam){
        try {
            ListQueuesRequest listQueuesRequest;
            if(searchParam.isEmpty()){
                listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix(searchParam).build();
            } else {
                listQueuesRequest = ListQueuesRequest.builder().build();
            }
            ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);

            for (String url : listQueuesResponse.queueUrls()) {
                System.out.println(url);
            }
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
