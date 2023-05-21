package com.app.design.gcp.pubsub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PubSubSubscriber {

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // Set the project ID and credentials file path
        String projectId = "your-project-id";
        String credentialsFilePath = "/path/to/credentials.json";

        // Load the credentials file
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                PubSubSubscriber.class.getResourceAsStream(credentialsFilePath));

        // Set the credentials provider
        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);

        // Set the subscription name
        String subscriptionId = "your-subscription-id";
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);

        // Create the subscriber
        Subscriber subscriber = Subscriber.newBuilder(subscriptionName, new MessagReceiverImpl())
                .setCredentialsProvider(credentialsProvider)
                .build();

        // Start the subscriber
        subscriber.startAsync().awaitRunning();

        // Keep the subscriber running for a specified duration
        long duration = 1L; // Duration in minutes
        subscriber.awaitTerminated(duration, TimeUnit.MINUTES);
    }

    private static class MessagReceiverImpl implements MessageReceiver {
        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
            // Process the received message
            String messageId = message.getMessageId();
            String messageData = message.getData().toStringUtf8();
            System.out.println("Received message: ID = " + messageId + ", Data = " + messageData);

            // Acknowledge the message
            consumer.ack();
        }
    }
}
