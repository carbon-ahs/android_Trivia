package com.axiagroups.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.axiagroups.trivia.controller.AppController;
import com.axiagroups.trivia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;

public class Repository {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public ArrayList<Question> getQuestions(final AnswerAsyncResponse callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
//                    Log.d("Main", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Question question = new Question(response.getJSONArray(i).get(0).toString(), response.getJSONArray(i).getBoolean(1));
                            questionArrayList.add(question);
//                            Log.d("Main", String.valueOf(response.getJSONArray(i).get(0)));
//                            Log.d("Main", String.valueOf(response.getJSONArray(i).getBoolean(1)));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(callback != null) callback.processFinished(questionArrayList);
                },
                error -> {
                    Log.d("Main", "Failed");
                });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
