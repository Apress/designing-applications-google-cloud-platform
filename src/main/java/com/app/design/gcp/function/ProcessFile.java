package com.app.design.gcp.function;

import com.google.api.services.storage.model.StorageObject;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;

import java.io.IOException;

public class ProcessFile implements BackgroundFunction<StorageObject> {

	@Override
	public void accept(StorageObject object, Context context) throws Exception {
		// TODO Auto-generated method stub
	    String bucket = object.getBucket();
	    String file = object.getName();
	    System.out.println("Processing file: " + file + " in bucket " + bucket);
	}
}
