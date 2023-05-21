package com.app.design.gcp.function;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class GetQuizFunction implements HttpFunction {
    private Firestore db;
    private Random random;
    private Gson gson;

    public GetQuizFunction() throws IOException {
    	  // Load the service account credentials file
        InputStream serviceAccount = getClass().getResourceAsStream("/path/to/service-account.json");

        // Set up FirestoreOptions with the loaded credentials
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        
     // Initialize Firestore with the options
        db = options.getService();
        
        random = new Random();
        gson = new Gson();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException, InterruptedException, ExecutionException {
        List<JsonObject> easyQuestions = new ArrayList<>();
        List<JsonObject> hardQuestions = new ArrayList<>();

        // Get all questions from the Firestore database
        QuerySnapshot questionsSnapshot = db.collection("quiz").get().get();
        for (QueryDocumentSnapshot question : questionsSnapshot) {
            JsonObject jsonQuestion = gson.fromJson(gson.toJson(question), JsonObject.class);
            String difficulty = jsonQuestion.get("difficulty").getAsString();
            if (difficulty.equals("easy")) {
                easyQuestions.add(jsonQuestion);
            } else if (difficulty.equals("hard")) {
                hardQuestions.add(jsonQuestion);
            }
        }

        // Select 5 random easy questions
        List<JsonObject> selectedEasyQuestions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(easyQuestions.size());
            selectedEasyQuestions.add(easyQuestions.get(index));
            easyQuestions.remove(index);
        }

        // Select 5 random hard questions
        List<JsonObject> selectedHardQuestions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(hardQuestions.size());
            selectedHardQuestions.add(hardQuestions.get(index));
            hardQuestions.remove(index);
        }

        // Combine the selected easy and hard questions
        List<JsonObject> selectedQuestions = new ArrayList<>();
        selectedQuestions.addAll(selectedEasyQuestions);
        selectedQuestions.addAll(selectedHardQuestions);

        // Convert the selected questions to JSON and set the content type to JSON
        String json = gson.toJson(selectedQuestions);
        response.setContentType("application/json");
        response.setStatusCode(HttpStatus.SC_OK);
		response.getWriter().write(json);
			
    	}
	}