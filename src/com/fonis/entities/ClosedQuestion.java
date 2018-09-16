package com.fonis.entities;

public class ClosedQuestion extends AbstractQuestion{

    @Override
    public boolean isAnswerCorrect(){
        return false;
    }
}
