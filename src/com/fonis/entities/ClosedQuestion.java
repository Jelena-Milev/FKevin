package com.fonis.entities;

import com.fonis.resources.Resources;

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
            throw new IllegalArgumentException("One or more possible answers are null or empty.");
        }
        this.possibleAnswers = possibleAnswers;
    }

    // #TODO check for duplicate answers
    public boolean validatePossibleAnswers(String[] possibleAnswers){
        if(possibleAnswers == null || possibleAnswers.length != 3){
            return false;
        }
        for(String possibleAnswer: possibleAnswers){
            if(!this.validateTextAttribute(possibleAnswer)){
                return false;
            }
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
        if(!this.validateDifficulty(this.difficulty)){
            return false;
        }
        return true;
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
