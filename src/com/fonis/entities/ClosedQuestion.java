package com.fonis.entities;

import com.fonis.resources.Resources;

import java.util.LinkedList;

public class ClosedQuestion extends AbstractQuestion{

    private String[] possibleAnswers;

    public ClosedQuestion(){
        this.possibleAnswers = new String[3];
    }

    public String[] getPossibleAnswers(){
        return possibleAnswers;
    }

    public void setPossibleAnswers(String[] possibleAnswers){
        if(!this.validatePossibleAnswers(possibleAnswers)){
            throw new IllegalArgumentException("One or more possible answers are null or empty or there is duplicated answers.");
        }
        this.possibleAnswers = possibleAnswers;
    }

    public boolean validatePossibleAnswers(String[] possibleAnswers){
        if(possibleAnswers == null || possibleAnswers.length != 3){
            return false;
        }
        for(String possibleAnswer: possibleAnswers){
            if(!this.validateTextAttribute(possibleAnswer) ||
                    possibleAnswer.toLowerCase().equals(this.correctAnswer.toLowerCase())){
                return false;
            }
        }
        if(getDuplicatedPossibleAnswer(possibleAnswers)!=-1){
            return false;
        }

        return true;
    }

    @Override
    public boolean validateQuestion(){
        if(!this.validateTextAttribute(this.questionText)){
            return false;
        }
        if(!this.validateTextAttribute(this.correctAnswer)){
            return false;
        }
        if(!this.validateTextAttribute(this.guessedAnswer)){
            return false;
        }
        if(!this.validatePossibleAnswers(this.possibleAnswers)){
            return false;
        }
        return true;
    }

    private int getDuplicatedPossibleAnswer(String[] possibleAnswers) {
        for (int i = 0; i < possibleAnswers.length - 1; i++) {
            for (int j = i + 1; j < possibleAnswers.length; j++) {
                if (possibleAnswers[i].toLowerCase().equals(possibleAnswers[j].toLowerCase()))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isAnswerCorrect(){
        return this.guessedAnswer.toLowerCase().equals(this.correctAnswer.toLowerCase());
    }

    @Override
    public int getPointsAfterValidation(){
        if(!this.validateQuestion()){
            throw new IllegalStateException("Question does not have all attributes set.");
        }
        return this.getQuestionPoints();
    }

    @Override
    public String toString(){
        return this.questionText;
    }

}
