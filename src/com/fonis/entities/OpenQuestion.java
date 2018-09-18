package com.fonis.entities;

import com.fonis.resources.Resources;

public class OpenQuestion extends AbstractQuestion {


    public void setQuestionText(String questionText) {
        if(!validateTextAttribute(questionText))
            throw new IllegalArgumentException("Question text is either null or empty.");
        this.questionText=questionText;
    }


    public void setCorrectAnswer(String correctAnswer) {
        if(!validateTextAttribute(correctAnswer))
            throw new IllegalArgumentException("Correct answer is either null or empty.");
        this.correctAnswer=correctAnswer;
    }


    public void setGuessedAnswer(String guessedAnswer) {
        if(!validateTextAttribute(guessedAnswer))
            throw new IllegalArgumentException("Guessed answer is either null or empty.");
        this.guessedAnswer=guessedAnswer;
    }

    public void setDifficulty(Resources.QuestionDifficulty difficulty) {
        if(!validateDifficulty(difficulty))
            throw new IllegalArgumentException("Question difficulty cannot be null");
        this.difficulty=difficulty;
    }

    @Override
    public boolean isAnswerCorrect() {
        String[] versionsOfCorrectAnswers = correctAnswer.split(";");
        for (String singleCorrectAnswer : versionsOfCorrectAnswers) {
            if (singleCorrectAnswer.toLowerCase().equals(this.guessedAnswer.toLowerCase()))
                return true;
        }
        return false;
    }

    @Override
    public int getPointsAfterValidation() {
        if (!validateQuestion())
            throw new IllegalStateException("Question does not have all attributes set. ");
        return getQuestionPoints();
    }

    @Override
    public boolean validateQuestion() {
        if (!validateTextAttribute(this.questionText)) {
            return false;
        }
        if (!validateTextAttribute(this.correctAnswer)) {
            return false;
        }
        if (!validateDifficulty(this.difficulty)) {
            return false;
        }
        if (!validateTextAttribute(this.guessedAnswer)) {
            return false;
        }
        return true;
    }
}
