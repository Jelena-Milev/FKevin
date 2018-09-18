package com.fonis.entities;

import com.fonis.resources.Resources;

public abstract class AbstractQuestion{
    protected String questionText;
    protected String correctAnswer;
    protected String guessedAnswer;
    protected Resources.QuestionDifficulty difficulty;

    protected abstract boolean isAnswerCorrect();


    public String getQuestionText(){
        return this.questionText;
    }

    public String getCorrectAnswer() { return this.correctAnswer; }

    public String getGuessedAnswer() { return this.guessedAnswer; }

    public Resources.QuestionDifficulty getDifficulty(){
        return this.difficulty;
    }

    public abstract int getPointsAfterValidation();

    protected int getQuestionPoints(){
        if(this.isAnswerCorrect()){
            if(this.difficulty == Resources.QuestionDifficulty.LOW){
                return Resources.QuestionDifficulty.LOW.getPoints();
            }else if(this.difficulty == Resources.QuestionDifficulty.MEDIUM){
                return Resources.QuestionDifficulty.MEDIUM.getPoints();
            }else{
                return Resources.QuestionDifficulty.HIGH.getPoints();
            }
        }else{
            return 0;
        }
    }

    public boolean validateTextAttribute(String text){
        return text != null && !text.isEmpty();
    }

    public boolean validateDifficulty(Resources.QuestionDifficulty questionDifficulty){
        return questionDifficulty != null;
    }

    public abstract boolean validateQuestion();
}
