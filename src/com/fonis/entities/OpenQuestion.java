package com.fonis.entities;

public class OpenQuestion extends AbstractQuestion {

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
