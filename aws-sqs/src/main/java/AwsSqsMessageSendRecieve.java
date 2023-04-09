import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AwsSqsMessageSendRecieve {
    static String queueUrl = "https://sqs.ap-south-1.amazonaws.com/615839970612/Shamit";

    public static void main(String[] args) {
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
        Employee employee = new Employee("472842", "Shamit", "IT", 2);
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = Obj.writeValueAsString(employee);
            System.out.println(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Sending message");
        sendMessage(sqsClient, jsonStr);

        System.out.println("Do you want to receive the message from queue ? Enter y/n ");
        Scanner user_input = new Scanner(System.in);
        String userInput = user_input.nextLine();
        if (userInput.equalsIgnoreCase("y")) {
            receiveMessage(sqsClient);
        }

    }

    public static void sendMessage(SqsClient sqsClient, String str) {

        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(str)
                .delaySeconds(10)
                .build());
    }

    public static void receiveMessage(SqsClient sqsClient) {
        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(5)
                    .build();
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            System.out.println(messages.size());
            List<String> lst = new ArrayList<>();
            messages.forEach(message -> {
                System.out.println(lst.add(message.body()));
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build();
                sqsClient.deleteMessage(deleteMessageRequest);
            });
            lst.forEach(s-> {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    System.out.println(s);
                    Employee emp2 = mapper.readValue(s, Employee.class);
                    System.out.println(emp2.toString());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}

class Employee {
    public String employeeId;
    public String name;
    public String dept;
    public int age;

    public Employee() {
    }

    public Employee(String employeeId, String name, String dept, int age) {
        this.employeeId = employeeId;
        this.name = name;
        this.dept = dept;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", dept='" + dept + '\'' +
                ", age=" + age +
                '}';
    }
}
