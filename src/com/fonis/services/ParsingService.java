package com.fonis.services;

import com.fonis.entities.Participant;
import com.fonis.entities.Question;
import com.fonis.resources.Resources;
import com.fonis.resources.Resources.EntityType;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParsingService {

    private Gson gson;

    public ParsingService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List getEntitiesAsList(EntityType entityType, String fileName) {
        Class type = entityType.equals(EntityType.Pariticipant) ? Participant.class : Question.class;
        Type collectionType = TypeToken.getParameterized(List.class, type).getType();
        JsonArray entitiesInJsonArray = getEntitiesAsJsonArray(entityType, fileName);
        List entitiesInList = gson.fromJson(entitiesInJsonArray, collectionType);
        return entitiesInList;
    }


    public JsonArray getEntitiesAsJsonArray(EntityType entityType, String fileName) {
        String propertyName = entityType.toString() + 's';
        JsonArray entitiesInJsonArray = null;
        JsonObject jsonFileContent = getFileContentAsJsonObject(fileName);
        if (jsonFileContent != null && jsonFileContent.has(propertyName)) {
            entitiesInJsonArray = jsonFileContent.getAsJsonArray(propertyName);
        }
        return entitiesInJsonArray;
    }

    public JsonObject getFileContentAsJsonObject(String fileName) {
        JsonObject fileContent = null;
        try (JsonReader jsonReader = new JsonReader(new FileReader(fileName))) {
            fileContent = gson.fromJson(jsonReader, JsonObject.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }


    /**
     * This method sets new value for given property in given file.
     * @param propertyName - Name of the property which value is changing
     * @param newPropertyValue - New value for the given property
     * @param fileName - File containing property which value is changing
     */
    private void changeValueOfPropertyInJsonFile(String propertyName, JsonElement newPropertyValue,
                                                    String fileName, boolean backUpFile) {
        if(backUpFile)
        backupFile(fileName);

        JsonObject jsonFileContent = getFileContentAsJsonObject(fileName);

//      If the file is empty, we have to create an object which we are going to write into the file.
        if (jsonFileContent == null)
            jsonFileContent = new JsonObject();

//      If an object from the file has a property named "Questions", we have to remove it in order to add it again
//      with a changed value. If the object has not such a property, there is nothing to be removed, and we only have
//      to add such a property.
        if (jsonFileContent.has(propertyName))
            jsonFileContent.remove(propertyName);

        jsonFileContent.add(propertyName, newPropertyValue);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(gson.toJson(jsonFileContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void backupFile(String fileName){

        File fileForBackup = new File(fileName);
        String[] fileNameParts=fileForBackup.getName().split("[.]");

        if(fileForBackup.exists()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = dateTimeFormatter.format(LocalDateTime.now());
            File backupFile = new File("backup/"+fileNameParts[0]+"-backup-" + currentDateTime + ".json");
            try {
                Files.copy(fileForBackup.toPath(), backupFile.toPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds object given to the .json file given
     * @param newEntity - object to be added to .json file
     * @param entityType - type of the object (Question or Participant)
     * @param fileName - name of the .json file in which object will be added
     * @throws Exception - if the given object already exists in the .json file
     */
    public void addEntityToJsonFile(Object newEntity, EntityType entityType,
                                    String fileName, boolean backUpFile) throws Exception {
        JsonObject newEntityAsJsonObject=gson.toJsonTree(newEntity).getAsJsonObject();
        JsonArray entitiesInJsonArray = getEntitiesAsJsonArray(entityType, fileName);

        if (entitiesInJsonArray == null)
            entitiesInJsonArray = new JsonArray();

        if (entitiesInJsonArray.contains(newEntityAsJsonObject))
            throw new Exception("This entity already exist in the file!");

        entitiesInJsonArray.add(newEntityAsJsonObject);

        String propertyName = entityType.toString() + 's';

        changeValueOfPropertyInJsonFile(propertyName, entitiesInJsonArray, fileName, backUpFile);
    }


    public void removeEntityFromJsonFile(Object entityForRemoving, Resources.EntityType entityType,
                                         String fileName, boolean backUpFile) throws Exception {
       JsonObject entityForRemovingAsJsonObject=gson.toJsonTree(entityForRemoving).getAsJsonObject();

        JsonArray jsonArrayOfEntities = getEntitiesAsJsonArray(entityType, fileName);

        if (jsonArrayOfEntities == null || jsonArrayOfEntities.size() == 0)
            throw new Exception("File is empty! There is no entities to be removed!");

        if (!jsonArrayOfEntities.contains(entityForRemovingAsJsonObject))
            throw new Exception("The entity does not exist in the file!");

        jsonArrayOfEntities.remove(entityForRemovingAsJsonObject);

        String propertyName=entityType.toString()+'s';
        changeValueOfPropertyInJsonFile(propertyName, jsonArrayOfEntities, fileName, backUpFile);
    }

    public void editExistingQuestion(Question questionForEditing,
                                     Question newQuestion, List<Question> questions) {

        if(checkForDuplicates(questionForEditing, newQuestion, questions)!=null)
            throw new RuntimeException("This question already exists!");

        questionForEditing.editQuestion(newQuestion);

        JsonElement questionsAsJsonElement = gson.toJsonTree(questions);
        changeValueOfPropertyInJsonFile("Questions", questionsAsJsonElement, "data/questions.json", true);
    }

    private Question checkForDuplicates(Question questionForEditing,
                                       Question newQuestion, List<Question> questions){
        for (Question question :
                questions) {
            if(question!=questionForEditing && question.equals(newQuestion))
                return question;
        }
        return null;
    }



}
