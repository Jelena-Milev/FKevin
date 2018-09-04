package com.fonis.entities;

import java.util.LinkedList;

public class Question {
    private enum questionDifficulty{
        Low, Medium, High
    }

    private enum questionType{
        Open, Closed
    }

    private String questionText;
    private String correctAnswer;
    private LinkedList<String> possibleAnswers;
    private questionDifficulty difficulty;
    private questionType type;

    public Question(String questionText, String correctAnswer, LinkedList<String> possibleAnswers, questionDifficulty difficulty, questionType type) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.possibleAnswers = possibleAnswers;
        this.difficulty = difficulty;
        this.type = type;
    }

    public Question(){
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        if(questionText.isEmpty())
            throw new RuntimeException("Question text have to be entered!");
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        if(correctAnswer.isEmpty())
            throw new RuntimeException("The correct answer have to be entered!");
        this.correctAnswer = correctAnswer;
    }

    public LinkedList<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(LinkedList<String> possibleAnswers) {
        if(possibleAnswers==null)
            throw new RuntimeException("There is no possible answers entered!");
        int indexOfNullAnswer=possibleAnswers.indexOf(null);
        if(indexOfNullAnswer!=-1)
            throw new RuntimeException("Possible answer at index "+indexOfNullAnswer+" is null!");
        if(possibleAnswers.size()<3)
            throw new RuntimeException("List with possible answers have to contain at least 3 possible answers!");
        int indexOfCorrectAnswer=possibleAnswers.indexOf(correctAnswer);
        if(indexOfCorrectAnswer!=-1)
            throw new RuntimeException("Possible answer at index "+indexOfCorrectAnswer+" is the correct answer! List with possible answers cannot contain correct answer!");
        this.possibleAnswers = possibleAnswers;
    }

    public questionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(questionDifficulty difficulty) {
        if(difficulty==null)
            throw new RuntimeException("Difficulty of the question have to be entered!");
        this.difficulty = difficulty;
    }

    public questionType getType() {
        return type;
    }

    public void setType(questionType type) {
        if(type==null)
            throw new RuntimeException("Type of the question have to be entered!");
        this.type = type;
    }

}
