package com.app.design.gcp.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GCSDownloadExample {
  public static void main(String[] args) throws IOException {
    // Authenticate with GCS using a service account
    Storage storage = StorageOptions.newBuilder()
        .setCredentials(GoogleCredentials.fromStream(new FileInputStream("path/to/credentials.json")))
        .build()
        .getService();

    // Create a BlobId object representing the file you want to download
    String bucketName = "my-bucket";
    String fileName = "path/to/file.txt";
    BlobId blobId = BlobId.of(bucketName, fileName);

    // Create a Blob object representing the file you want to download
    Blob blob = storage.get(blobId);

    // Use the Blob.downloadTo method to download the file from GCS
    FileOutputStream outputStream = new FileOutputStream("path/to/download/location/file.txt");
    blob.downloadTo(outputStream);
    outputStream.close();
  }
}
