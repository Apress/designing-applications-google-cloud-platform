package com.app.design.gcp.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GCSDeleteExample {
  public static void main(String[] args) throws FileNotFoundException, IOException {
    // Authenticate with GCS using a service account
    Storage storage = StorageOptions.newBuilder()
        .setCredentials(GoogleCredentials.fromStream(new FileInputStream("path/to/credentials.json")))
        .build()
        .getService();

    // Create a BlobId object representing the file you want to delete
    String bucketName = "my-bucket";
    String fileName = "path/to/file.txt";
    BlobId blobId = BlobId.of(bucketName, fileName);

    // Use the Storage.delete method to delete the file from GCS
    boolean deleted = storage.delete(blobId);
    if(deleted) {
        System.out.println("File deleted successfully!");
    } else {
        System.out.println("File deletion failed!");
    }
  }
}