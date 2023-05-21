package com.app.design.gcp.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.IOException;

public class DownloadFile {

    public static void main(String[] args) throws IOException {
        // create a storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // specify the bucket and file name
        String bucketName = "my-bucket";
				String fileName = "file.txt";

        // create a blob id
        BlobId blobId = BlobId.of(bucketName, fileName);

        // create a blob object
        Blob blob = storage.get(blobId);

        // download the file
        File file = new File(fileName);
        blob.downloadTo(file.toPath());
    }
}
