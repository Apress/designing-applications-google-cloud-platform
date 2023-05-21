package com.app.design.gcp.pubsub;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class PubSubClient {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // Set the project ID and credentials file path
        String projectId = "your-project-id";
        String credentialsFilePath = "/path/to/credentials.json";

        // Load the credentials file
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                PubSubClient.class.getResourceAsStream(credentialsFilePath));

        // Set the credentials provider
        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);

        // Set the topic name
        TopicName topicName = TopicName.of(projectId, "your-topic-name");

        // Create the publisher
        Publisher publisher = Publisher.newBuilder(topicName)
                .setCredentialsProvider(credentialsProvider)
                .build();

        // Use the publisher to publish messages
        ByteString data = ByteString.copyFromUtf8("Hello, Cloud Pub/Sub!");
        com.google.pubsub.v1.PubsubMessage pubsubMessage = com.google.pubsub.v1.PubsubMessage.newBuilder().setData(data).build();

        ApiFuture<String> future = publisher.publish(pubsubMessage);
        String messageId = future.get();
        System.out.println("Published message with ID: " + messageId);
        // ...
        
        // Shutdown the publisher when done
        publisher.shutdown();
    }
}

