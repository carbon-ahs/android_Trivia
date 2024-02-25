package com.axiagroups.trivia.data;

import com.axiagroups.trivia.model.Question;

import java.util.ArrayList;

public interface AnswerAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
