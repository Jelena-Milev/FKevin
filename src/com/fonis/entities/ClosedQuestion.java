package com.fonis.entities;

import com.fonis.resources.Resources;

public class ClosedQuestion extends AbstractQuestion{

    private String[] possibleAnswers;

    public ClosedQuestion(){
        this.possibleAnswers = new String[3];
    }

    public void setQuestionText(String questionText){
        if(!this.validateTextAttribute(questionText)){
            throw new IllegalArgumentException("Question text is either null or empty.");
        }
        this.questionText = questionText;
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
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof ClosedQuestion)) return false;
        ClosedQuestion that = (ClosedQuestion) o;
        return this.questionText.toLowerCase().equals(that.questionText.toLowerCase());
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Question Text: ").append(this.questionText);
        stringBuilder.append("\nPossible answers: \n");
        for(int i = 0; i < this.possibleAnswers.length; ++i){
            stringBuilder.append(i).append(") ").append(this.possibleAnswers[i]).append("\n");
        }
        stringBuilder.append("Guessed answer: ").append(this.guessedAnswer);
        stringBuilder.append("\nCorrect answer: ").append(this.correctAnswer);
        return stringBuilder.toString();
    }

}
