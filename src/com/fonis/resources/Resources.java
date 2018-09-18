package com.fonis.resources;

import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.entities.Participant;

public class Resources {

    // #TODO set this to the location the application is running from, dynamically
    public static String APP_LOCATION = "";

    public static final String DATA_LOCATION = APP_LOCATION + "data/";
    public static final String DATA_BACKUP_LOCATION = DATA_LOCATION + "backup/";

    // Entity names
    public enum Entities{
        PARTICIPANT("Participants", "Participants.json", Participant.class),
        OPEN_QUESTION("OpenQuestions", "Questions.json", OpenQuestion.class),
        CLOSED_QUESTION("ClosedQuestions", "Questions.json", ClosedQuestion.class);

        private final String entityName;
        private final String jsonFileName;
        private final Class entityClass;

        Entities(String entityName, String jsonFieName, Class entityClass){
            this.entityName = entityName;
            this.jsonFileName = jsonFieName;
            this.entityClass = entityClass;
        }

        public String getEntityName(){
            return this.entityName;
        }

        public String getEntityJsonFileName(){
            return this.jsonFileName;
        }

        public Class getEntityClass(){
            return this.entityClass;
        }
    }

    public enum QuestionDifficulty {
        LOW (1),
        MEDIUM (3),
        HIGH (5);

        private final int points;

        QuestionDifficulty(int points){
            this.points = points;
        }

        public int getPoints(){
            return this.points;
        }
    }

    // #TODO to be removed together with Question class
    @Deprecated
    public enum EntityType{
        Question, Pariticipant
    }
}
