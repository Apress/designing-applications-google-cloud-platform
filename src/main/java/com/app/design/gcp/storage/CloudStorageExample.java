
/// Import the Google Cloud Storage Java Client library
package com.app.design.gcp.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class CloudStorageExample {

    public static void main(String[] args) {
        // Create a storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // List all of the buckets in the project
        for (Bucket bucket : storage.list().iterateAll()) {
            System.out.println(bucket.getName());
        }
    }
    
    public void createBucket(String bucketName) {
        // Connect to cloud storage
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Create a new bucket
        Bucket bucket = storage.create(BucketInfo.of(bucketName));
        System.out.println("Bucket " + bucket.getName() + " created.");
    }
    
    public void uploadFile(String bucketName, String filePath) throws FileNotFoundException {
        // Connect to cloud storage
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Get the bucket
        Bucket bucket = storage.get(bucketName);

        // Upload a file to the bucket
        bucket.create(filePath, new FileInputStream(filePath), Bucket.BlobWriteOption.userProject("your-project-id"));
        System.out.println("File uploaded to bucket.");
    }
}