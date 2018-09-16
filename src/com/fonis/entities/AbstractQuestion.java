package com.fonis.entities;

import com.fonis.resources.Resources;

public abstract class AbstractQuestion{
    private String questionText;
    private String correctAnswer;
    private Resources.QuestionDifficulty difficulty;

    public abstract boolean isAnswerCorrect();

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

    public Resources.QuestionDifficulty getDifficulty(){
        return this.difficulty;
    }

    public String getQuestionText(){
        return this.questionText;
    }
}
