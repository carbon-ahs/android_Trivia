package com.axiagroups.trivia.model;

public class Question {
    private String statement;
    private boolean answer;

    public Question() {
    }

    public Question(String statement, boolean answer) {
        this.statement = statement;
        this.answer = answer;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
