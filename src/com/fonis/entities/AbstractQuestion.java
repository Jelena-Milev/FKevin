package com.fonis.entities;

import com.fonis.resources.Resources;

public abstract class AbstractQuestion {
    protected String questionText;
    protected String correctAnswer;
    protected String guessedAnswer;
    protected Resources.QuestionDifficulty difficulty;

    protected abstract boolean isAnswerCorrect();

    public abstract int getPointsAfterValidation();

    public abstract boolean validateQuestion();

    public String getQuestionText() {
        return this.questionText;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public String getGuessedAnswer() {
        return this.guessedAnswer;
    }

    public Resources.QuestionDifficulty getDifficulty() {
        return this.difficulty;
    }

    public void setQuestionText(String questionText){
        if(!this.validateTextAttribute(questionText)){
            throw new IllegalArgumentException("Question text is either null or empty.");
        }
        this.questionText = questionText;
    }

    public void setCorrectAnswer(String correctAnswer){
        if(!this.validateTextAttribute(correctAnswer)){
            throw new IllegalArgumentException("Correct answer is either null or empty.");
        }
        this.correctAnswer = correctAnswer;
    }

    public void setGuessedAnswer(String guessedAnswer){
        if(!this.validateTextAttribute(guessedAnswer)){
            throw new IllegalArgumentException("Guessed answer is either null or empty.");
        }
        this.guessedAnswer = guessedAnswer;
    }

    public void setDifficulty(Resources.QuestionDifficulty questionDifficulty){
        if(!this.validateDifficulty(questionDifficulty)){
            throw new IllegalArgumentException("Question difficulty cannot be null");
        }
        this.difficulty = questionDifficulty;
    }

    protected int getQuestionPoints() {
        if (this.isAnswerCorrect()) {
            if (this.difficulty == Resources.QuestionDifficulty.LOW) {
                return Resources.QuestionDifficulty.LOW.getPoints();
            } else if (this.difficulty == Resources.QuestionDifficulty.MEDIUM) {
                return Resources.QuestionDifficulty.MEDIUM.getPoints();
            } else {
                return Resources.QuestionDifficulty.HIGH.getPoints();
            }
        } else {
            return 0;
        }
    }

    public boolean validateTextAttribute(String text) {
        return text != null && !text.isEmpty();
    }

    public boolean validateDifficulty(Resources.QuestionDifficulty questionDifficulty) {
        return questionDifficulty != null;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof AbstractQuestion)) return false;
        AbstractQuestion that = (AbstractQuestion) o;
        return this.questionText.toLowerCase().equals(that.questionText.toLowerCase());
    }
}
