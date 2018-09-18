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

    public int getQuestionPoints(){
        if(this.isAnswerCorrect()){
            if(this.difficulty == Resources.QuestionDifficulty.Low){
                return Resources.DifficultyPoints.Low.getPoints();
            }else if(this.difficulty == Resources.QuestionDifficulty.Medium){
                return Resources.DifficultyPoints.Medium.getPoints();
            }else{
                return Resources.DifficultyPoints.High.getPoints();
            }
        }else{
            return 0;
        }
    }
}
