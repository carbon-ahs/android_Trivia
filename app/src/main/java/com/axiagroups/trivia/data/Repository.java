package com.axiagroups.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.axiagroups.trivia.controller.AppController;
import com.axiagroups.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<List> getQuestion() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("Main", response.toString());
                },
                error -> {
                    Log.d("Main", "Failed");
                });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return null;
    }
}
