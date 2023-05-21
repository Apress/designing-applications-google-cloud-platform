package com.app.design.gcp.storage;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public static void main(String[] args) throws IOException {
        // specify the file name
        String fileName = "file.txt";

        // create a file object
        File file = new File(fileName);

        // create a new file
        file.createNewFile();

        // check if the file exists
        if (file.exists()) {
            System.out.println("File exists");
        }

        // get the file size
        long fileSize = file.length();
        System.out.println("File size: " + fileSize);
        
        //rename file
        file.renameTo(new File("newName.txt"));

        // delete the file
        file.delete();

        // check if the file was deleted
        if (!file.exists()) {
            System.out.println("File deleted");
        }
    }
    
    /// Some file operation on each file in the directory
    public void fileOpOnDir() {
    	File dir = new File("/path/to/directory");
    	if(dir.isDirectory()){
    	    File[] files = dir.listFiles();
    	    for(File f : files){
    	        //do something with file
    	    	//print the file path
    	    	System.out.println(f.getPath());
    	    }
    	}
    }
}
