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
}
