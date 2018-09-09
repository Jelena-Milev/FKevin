package com.fonis.entities;

import java.util.LinkedList;
import java.util.Objects;

public class Question {
    public enum questionDifficulty {
        Low, Medium, High
    }

    //closed questions are pick an answers questions and open questions are fill in questions
    public enum questionType {
        Open, Closed
    }

    private questionType type;
    private String questionText;
    private String correctAnswer;
    private LinkedList<String> possibleAnswers;
    private questionDifficulty difficulty;

    public Question() {
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        if (questionText == null || questionText.isEmpty())
            throw new RuntimeException("Question text have to be entered!");
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        if (correctAnswer == null || correctAnswer.isEmpty())
            throw new RuntimeException("The correct answer have to be entered!");
        this.correctAnswer = correctAnswer;
    }

    public LinkedList<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(LinkedList<String> possibleAnswers) {
        if (type == questionType.Open) {
            if (possibleAnswers != null && possibleAnswers.size() > 0)
                throw new RuntimeException("Check again, open questions cannot have offered answers to choose one of them!");
        } else {
            int indexOfInvalidAnswer;
            if (possibleAnswers == null)
                throw new RuntimeException("There is no possible answers entered!");
            if (possibleAnswers.size() < 3)
                throw new RuntimeException("List with possible answers have to contain at least 3 possible answers!");
            indexOfInvalidAnswer = possibleAnswers.indexOf(null);
            if (indexOfInvalidAnswer != -1)
                throw new RuntimeException("Possible answer at index " + indexOfInvalidAnswer + " is null!");
            indexOfInvalidAnswer = possibleAnswers.indexOf("");
            if (indexOfInvalidAnswer != -1)
                throw new RuntimeException("Possible answer at index " + indexOfInvalidAnswer + " is an empty answer!");
            indexOfInvalidAnswer = possibleAnswers.indexOf(correctAnswer);
            if (indexOfInvalidAnswer != -1)
                throw new RuntimeException("Possible answer at index " + indexOfInvalidAnswer + " is the correct answer! List with possible answers cannot contain correct answer!");
            indexOfInvalidAnswer = duplicatedAnswer(possibleAnswers);
            if (indexOfInvalidAnswer != -1)
                throw new RuntimeException("Possible answer at index " + indexOfInvalidAnswer + " has a duplicate amongst other answers!");
        }
        this.possibleAnswers = possibleAnswers;
    }

    public questionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(questionDifficulty difficulty) {
        if (difficulty == null)
            throw new RuntimeException("Difficulty of the question have to be entered!");
        this.difficulty = difficulty;
    }

    public questionType getType() {
        return type;
    }

    public void setType(questionType type) {
        if (type == null)
            throw new RuntimeException("Type of the question have to be entered!");
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", difficulty=" + difficulty +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return type == question.type &&
                questionText.equalsIgnoreCase(question.questionText) &&
                correctAnswer.equalsIgnoreCase(question.correctAnswer) &&
                Objects.equals(possibleAnswers, question.possibleAnswers) &&
                difficulty == question.difficulty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, questionText, correctAnswer, possibleAnswers, difficulty);
    }

    private int duplicatedAnswer(LinkedList<String> answers) {
        for (int i = 0; i < answers.size() - 1; i++) {
            for (int j = i + 1; j < answers.size(); j++) {
                if (answers.get(i).equalsIgnoreCase(answers.get(j)))
                    return i;
            }
        }
        return -1;
    }

    public void editQuestion(questionType newQuestionType, String newQuestionText, String newCorrectAnswer,
                             LinkedList<String> newPossibleAnswers, questionDifficulty newQuestionDifficulty) {

        if (!type.equals(newQuestionType))
            setType(newQuestionType);

        if (!questionText.equalsIgnoreCase(newQuestionText))
            setQuestionText(newQuestionText);

        if (!correctAnswer.equalsIgnoreCase(newCorrectAnswer))
            setCorrectAnswer(newCorrectAnswer);

        if (this.type.equals(questionType.Open) && newPossibleAnswers != null) {
            newPossibleAnswers = null;
        }
        if (this.type.equals(questionType.Closed) && newPossibleAnswers == null){
           newPossibleAnswers=new LinkedList<>();
        }
        this.setPossibleAnswers(newPossibleAnswers);


        if (!difficulty.equals(newQuestionDifficulty))
            setDifficulty(newQuestionDifficulty);
    }


}
