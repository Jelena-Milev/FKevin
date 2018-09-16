package com.fonis.resources;

public class Resources {


    public enum QuestionDifficulty {
        Low, Medium, High
    }

    //closed questions are pick an answers questions and open questions are fill in questions
    public enum QuestionType {
        Open, Closed
    }

    public enum EntityType{
        Question, Pariticipant
    }

    public enum DifficultyPoints{
        Low (1),
        Medium (3),
        High (5);

        private int points;

        DifficultyPoints(int value){
            this.points = value;
        }

        public int getPoints(){
            return this.points;
        }

    }
}
