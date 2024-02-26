package com.axiagroups.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.axiagroups.trivia.data.AnswerAsyncResponse;
import com.axiagroups.trivia.data.Repository;
import com.axiagroups.trivia.databinding.ActivityMainBinding;
import com.axiagroups.trivia.model.Question;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static String MESSAGE_ID = "axia_trivia";

    private SharedPreferences sharedPreferences;

    private int currentHighScore;
    private int currentQuestionIndex = 0;
    private int currentScore = 0;
    List<Question> questionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        binding.prevBtn.setEnabled(false);


        sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        currentHighScore = sharedPreferences.getInt("high_score", 0);
        binding.highScoreTV.setText(String.format(getString(R.string.high_score_text), currentHighScore));
        binding.scoreTV.setText(String.format(getString(R.string.current_score_text), currentScore));
//        showMsgTV.setText(value);
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
            updateQuestionStatement();
            updateScores();
        });

        binding.falseBtn.setOnClickListener(v -> {
            checkAnswer(false);
            updateQuestionStatement();
            updateScores();
        });




    }

    private void checkAnswer(boolean userChoice) {
        boolean answer = questionList.get(currentQuestionIndex).getAnswer();
        int snackMessageId = 0;
        if (userChoice == answer) {
            snackMessageId = R.string.correct_answer;
            currentScore = currentScore + 10;
            fadeAnimation();
        }
        else {
            snackMessageId = R.string.wrong_answer;
            if(currentScore >= 5){
                currentScore = currentScore - 5;
            }
            shakeAnimation();
        }

        Snackbar.make(binding.cardView, snackMessageId, Snackbar.LENGTH_LONG).show();
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

    private void updateScores() {
        binding.scoreTV.setText(String.format(getString(R.string.current_score_text), currentScore));
        if(currentScore > currentHighScore){
            currentHighScore = currentScore;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("high_score", currentHighScore);
            editor.apply(); // saving
        }
        binding.highScoreTV.setText(String.format(getString(R.string.high_score_text), currentHighScore));
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTV.setTextColor(Color.RED);
//                binding.cardView.setB
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTV.setTextColor(Color.WHITE);
//                currentQuestionIndex++;
//                updateQuestionStatement();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTV.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTV.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}