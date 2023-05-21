package com.app.design.gcp.storage;

//Use of credential class to load service account key 

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class GCSExample {
    public static void main(String[] args) throws IOException {
        // Load the service account key
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("path/to/service_account.json"));
        // Create a storage client
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        // Use the storage client to interact with GCS
        // ...
        
        ///print the service account information for stotage associated with the GCP_PROJCT_ID
        System.out.println(storage.getServiceAccount("GCP_PROJECT_ID"));
    }
}
