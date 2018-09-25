package com.fonis.resources;

import com.fonis.entities.ClosedQuestion;
import com.fonis.entities.OpenQuestion;
import com.fonis.entities.Participant;
import sample.Main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.CodeSource;

public class Resources {

    // Prevents init of class
    private Resources(){}

    // #TODO set this to the location the application is running from, dynamically
    public static String APP_LOCATION = "";

    public static final String DATA_LOCATION = APP_LOCATION + "data/";
    public static final String DATA_BACKUP_LOCATION = DATA_LOCATION + "backup/";

    private static int lowQuestionPoints = 1;
    private static int mediumQuestionPoints = 3;
    private static int highQuestionPoints = 5;

    private static int numberOfLowQuestions = 5;
    private static int numberOfMediumQuestions = 4;
    private static int numberOfHighQuestions = 3;

    public static final int TOTAL_NUMBER_OF_QUESTIONS = numberOfLowQuestions + numberOfMediumQuestions + numberOfHighQuestions;


    // Entity names
    public enum Entities{
        PARTICIPANT("Participants", "email", "Participants.json", Participant.class),
        OPEN_QUESTION("OpenQuestions", "questionText", "Questions.json", OpenQuestion.class),
        CLOSED_QUESTION("ClosedQuestions", "questionText", "Questions.json", ClosedQuestion.class);

        private final String entityName;
        private final String identifyingAttribute;
        private final String jsonFileName;
        private final Class entityClass;

        Entities(String entityName, String identifyingAttribtue, String jsonFieName, Class entityClass){
            this.entityName = entityName;
            this.identifyingAttribute = identifyingAttribtue;
            this.jsonFileName = jsonFieName;
            this.entityClass = entityClass;
        }

        public String getEntityName(){
            return this.entityName;
        }

        public String getIdentifyingAttribute(){
            return this.identifyingAttribute;
        }

        public String getEntityJsonFileName(){
            return this.jsonFileName;
        }

        public Class getEntityClass(){
            return this.entityClass;
        }


        @Override
        public String toString() {
            return this.entityClass.getSimpleName();
        }
    }

    public enum QuestionDifficulty{
        LOW (lowQuestionPoints, numberOfLowQuestions),
        MEDIUM (mediumQuestionPoints, numberOfMediumQuestions),
        HIGH (highQuestionPoints, numberOfHighQuestions);

        private final int points;
        private final int numberOfQuestions;

        QuestionDifficulty(int points, int numberOfQuestions){
            this.points = points;
            this.numberOfQuestions = numberOfQuestions;
        }

        public int getPoints(){
            return this.points;
        }

        public int getNumberOfQuestions(){
            return this.numberOfQuestions;
        }
    }

    public static String getAppLocation(){
        CodeSource codeSource= Main.class.getProtectionDomain().getCodeSource();
        String path=codeSource.getLocation().getPath();
        File launcher=null;
        try {
            String decodedPath= URLDecoder.decode(path, "UTF-8");
            launcher=new File(decodedPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return launcher.getParentFile().getPath();
    }
}
