package com.axiagroups.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.axiagroups.trivia.data.AnswerAsyncResponse;
import com.axiagroups.trivia.data.Repository;
import com.axiagroups.trivia.databinding.ActivityMainBinding;
import com.axiagroups.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    List<Question> questionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        questionList = new Repository().getQuestions(new AnswerAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                Log.d("Main", "onCreate:" + questionArrayList);
                binding.questionTV.setText(questionArrayList.get(currentQuestionIndex).getStatement().toString());
                updateCounter(questionArrayList);
//                binding.outofTV.setText("Question: "+ 1 + "/" + questionList.size());
            }
        });

        binding.nextBtn.setOnClickListener(v -> {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
            updateQuestionStatement();
        });

        binding.prevBtn.setOnClickListener(v -> {
            if (currentQuestionIndex != 0) {
                currentQuestionIndex --;
                updateQuestionStatement();
            }
            else {
                Toast.makeText(MainActivity.this, "It\'s the first Question, no previous question available.", Toast.LENGTH_SHORT).show();
            }
        });
        
        binding.trueBtn.setOnClickListener(v -> {
            checkAnswer(true);
        });

        binding.falseBtn.setOnClickListener(v -> {
            checkAnswer(false);
        });




    }

    private void checkAnswer(boolean userChoice) {
        boolean answer = questionList.get(currentQuestionIndex).getAnswer();
        int snackMessageId = 0;
        if (userChoice == answer) {
            snackMessageId = R.string.carrect_answer;
        }
        else {
            snackMessageId = R.string.wrong_answer;
        }
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        String counterText = String.format(getString(R.string.question_counter), currentQuestionIndex + 1, questionArrayList.size());
        binding.outofTV.setText(counterText);
    }

    private void updateQuestionStatement() {
//        binding.questionTV.setText((CharSequence) questionList.get(currentQuestionIndex));
        String questionStatement = questionList.get(currentQuestionIndex).getStatement();
        updateCounter((ArrayList<Question>) questionList);
        binding.questionTV.setText(questionStatement);
    }
}