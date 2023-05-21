package com.app.design.gcp.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class GCSUploadExample {
  public static void main(String[] args) throws IOException {
    // Authenticate with GCS using a service account
    Storage storage = StorageOptions.newBuilder()
        .setCredentials(GoogleCredentials.fromStream(new FileInputStream("path/to/credentials.json")))
        .build()
        .getService();

    // Create a BlobId object representing the file you want to upload
    String bucketName = "my-bucket";
    String fileName = "path/to/file.txt";
    BlobId blobId = BlobId.of(bucketName, fileName);

    // Create a BlobInfo object representing the file you want to upload
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    
    //Returns an option that causes an operation to succeed only if the target blob does not exist.
    Storage.BlobWriteOption precondition = Storage.BlobWriteOption.doesNotExist();
    

    // Use the Blob.create method to upload the file to GCS
    File file = new File(fileName);
    Blob blob = storage.createFrom(blobInfo, Paths.get(file.getPath()),precondition);

    // Print the public URL of the uploaded file
    System.out.println("File uploaded to: " + blob.getMediaLink());
  }
}
