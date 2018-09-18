package com.fonis.entities;

import com.fonis.resources.Resources;

public class ClosedQuestion extends AbstractQuestion{

    private String[] possibleAnswers;

    public ClosedQuestion(){
        this.possibleAnswers = new String[3];
    }

    public void setQuestionText(String questionText){
        if(!this.validateText(questionText)){
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
        if(!this.validateText(correctAnswer)){
            throw new IllegalArgumentException("Correct answer is either null or empty.");
        }
        this.correctAnswer = correctAnswer;
    }

    public void setGuessedAnswer(String guessedAnswer){
        if(!this.validateText(guessedAnswer)){
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

    public boolean validateText(String text){
        return text != null && !text.isEmpty();
    }

    public boolean validatePossibleAnswers(String[] possibleAnswers){
        if(possibleAnswers == null || possibleAnswers.length != 3){
            return false;
        }
        for(int i = 0; i < possibleAnswers.length; ++i){
            if(!this.validateText(possibleAnswers[i])){
                return false;
            }
        }
        return true;
    }

    public boolean validateDifficulty(Resources.QuestionDifficulty questionDifficulty){
        return questionDifficulty != null;
    }

    @Override
    public boolean isAnswerCorrect(){
        return this.guessedAnswer.toLowerCase().equals(this.correctAnswer.toLowerCase());
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
