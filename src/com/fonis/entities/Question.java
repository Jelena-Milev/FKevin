package com.fonis.entities;

import com.fonis.resources.Resources;

import java.util.LinkedList;
import java.util.Objects;

@Deprecated
public class Question {

    private Resources.QuestionType type;
    private String questionText;
    private String correctAnswer;
    private LinkedList<String> possibleAnswers;
    private Resources.QuestionDifficulty difficulty;

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

    private void setQuestionTextNoValidation(String questionText) {
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

    private void setCorrectAnswerNoValidation(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public LinkedList<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(LinkedList<String> possibleAnswers) {
        if (type == Resources.QuestionType.Open) {
            if (possibleAnswers != null && possibleAnswers.size() > 0)
                throw new RuntimeException("Check again, open questions cannot have offered answers to choose one of them!");
        } else {
            validatePossibleAnswers(possibleAnswers);
        }
        this.possibleAnswers = possibleAnswers;
    }

    private void setPossibleAnswersNoValidation(LinkedList<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }


    public Resources.QuestionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Resources.QuestionDifficulty difficulty) {
        if (difficulty == null)
            throw new RuntimeException("Difficulty of the question have to be entered!");
        this.difficulty = difficulty;
    }

    private void setDifficultyNoValidation(Resources.QuestionDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Resources.QuestionType getType() {
        return type;
    }

    public void setType(Resources.QuestionType type) {
        if (type == null)
            throw new RuntimeException("Type of the question have to be entered!");
        this.type = type;
    }

    private void setTypeNoValidation(Resources.QuestionType type) {
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
        return questionText.equalsIgnoreCase(question.questionText);
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

    private void validatePossibleAnswers(LinkedList<String> possibleAnswers) {
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

    /**
     * Changes the attributes of this question with the attributes of the newQuestion
     * @param newQuestion - question which attributes are new values of this object's attributes
     */
    public void editQuestion(Question newQuestion) {
        Resources.QuestionType newType=newQuestion.getType();
        if(!this.type.equals(newType))
            this.setTypeNoValidation(newType);

        String newQuestionText=newQuestion.getQuestionText();
        if(!this.questionText.equalsIgnoreCase(newQuestionText))
            this.setQuestionTextNoValidation(newQuestionText);

        String newCorrectAnswer=newQuestion.getCorrectAnswer();
        if(!this.correctAnswer.equalsIgnoreCase(newCorrectAnswer))
            this.setCorrectAnswerNoValidation(newCorrectAnswer);

        LinkedList newPossibleAnswers=newQuestion.getPossibleAnswers();
        if(this.possibleAnswers!=null && !this.possibleAnswers.equals(newPossibleAnswers))
            this.setPossibleAnswersNoValidation(newPossibleAnswers);

        Resources.QuestionDifficulty newDifficulty=newQuestion.getDifficulty();
        if(!this.difficulty.equals(newDifficulty))
            this.setDifficultyNoValidation(newDifficulty);
    }
}
