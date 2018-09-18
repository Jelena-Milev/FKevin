package com.fonis.entities;

public class OpenQuestion extends AbstractQuestion{

    @Override
    public boolean isAnswerCorrect(){
        return false;
    }

    @Override
    public int getPointsAfterValidation(){
        return 0;
    }

    @Override
    public boolean validateQuestion(){
        return false;
    }
}
