package com.fonis.resources;

public class Resources {

    // #TODO set this to the location the applicaiton is running from, dynamically
    public static String APP_LOCATION = "";

    // Entity names
    public enum Entities{
        PARTICIPANT("Participants", "Participants"),
        OPEN_QUESTION("OpenQuestions", "Questions"),
        CLOSED_QUESTION("ClosedQuestions", "Questions");

        private final String entityName;
        private final String jsonFileName;

        Entities(String entityName, String jsonFieName){
            this.entityName = entityName;
            this.jsonFileName = jsonFieName;
        }

        public String getEntityName(){
            return this.entityName;
        }

        public String getEntityJsonFileName(){
            return this.jsonFileName;
        }
    }

    public enum QuestionDifficulty {
        LOW (1),
        MEDIUM (3),
        HIGH (5);

        private int points;

        QuestionDifficulty(int value){
            this.points = value;
        }

        public int getPoints(){
            return this.points;
        }
    }

    public enum EntityType{
        Question, Pariticipant
    }
}
