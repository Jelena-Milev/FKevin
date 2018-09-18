package com.fonis.services;

import com.fonis.entities.AbstractQuestion;
import com.fonis.resources.Resources;
import com.fonis.resources.Resources.Entities;
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

    public List getEntitiesAsList(Entities entity) {
        Type collectionType = TypeToken.getParameterized(List.class, entity.getEntityClass()).getType();
        JsonArray entitiesInJsonArray = getEntitiesAsJsonArray(entity);
        List entitiesInList = gson.fromJson(entitiesInJsonArray, collectionType);
        return entitiesInList;
    }


    public JsonArray getEntitiesAsJsonArray(Resources.Entities entity) {
        String propertyName = entity.getEntityName();
        JsonArray entitiesInJsonArray = null;
        String pathToJsonFile = Resources.DATA_LOCATION + entity.getEntityJsonFileName();
        JsonObject jsonFileContent = getFileContentAsJsonObject(pathToJsonFile);
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



//    private void changeValueOfPropertyInJsonFile(String propertyName, JsonElement newPropertyValue,
//                                                    String fileName, boolean backUpFile) {
    private void changeValueOfPropertyInJsonFile(Entities entity, JsonElement newPropertyValue,
                                                 boolean backUpFile) {

        String pathToJsonFile = Resources.DATA_LOCATION + entity.getEntityJsonFileName();
        if (backUpFile)
            backupFile(pathToJsonFile);

        JsonObject jsonFileContent = getFileContentAsJsonObject(pathToJsonFile);

//      If the file is empty, we have to create an object which we are going to write into the file.
        if (jsonFileContent == null)
            jsonFileContent = new JsonObject();

//      If an object from the file has a property named "Questions", we have to remove it in order to add it again
//      with a changed value. If the object has not such a property, there is nothing to be removed, and we only have
//      to add such a property.
        if (jsonFileContent.has(entity.getEntityName()))
            jsonFileContent.remove(entity.getEntityName());

        jsonFileContent.add(entity.getEntityName(), newPropertyValue);

        try (FileWriter writer = new FileWriter(pathToJsonFile)) {
            writer.write(gson.toJson(jsonFileContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void backupFile(String pathToOriginalFile) {

        File originalFile = new File(pathToOriginalFile);
        String[] fileNameParts = originalFile.getName().split("[.]");

        if (originalFile.exists()) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = dateTimeFormatter.format(LocalDateTime.now());
            File backupFile = new File(Resources.DATA_BACKUP_LOCATION + fileNameParts[0] + "-backup-" + currentDateTime + ".json");
            try {
                Files.copy(originalFile.toPath(), backupFile.toPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void addEntityToJsonFile(Object newEntity, Entities entity, boolean backUp)throws Exception {

        JsonObject newEntityAsJsonObject = gson.toJsonTree(newEntity).getAsJsonObject();
        JsonArray entitiesInJsonArray = getEntitiesAsJsonArray(entity);

        if (entitiesInJsonArray == null)
            entitiesInJsonArray = new JsonArray();

        if (entitiesInJsonArray.contains(newEntityAsJsonObject))
            throw new IllegalArgumentException("This entity already exist in the file!");

        entitiesInJsonArray.add(newEntityAsJsonObject);

        changeValueOfPropertyInJsonFile(entity, entitiesInJsonArray, backUp);
    }


    public void removeEntityFromJsonFile(Object entityForRemoving, Entities entity,
                                            boolean backUpFile) throws Exception {
        JsonObject entityForRemovingAsJsonObject = gson.toJsonTree(entityForRemoving).getAsJsonObject();

        JsonArray jsonArrayOfEntities = getEntitiesAsJsonArray(entity);

        if (jsonArrayOfEntities == null || jsonArrayOfEntities.size() == 0)
            throw new IllegalStateException("File is empty! There is no entities to be removed!");

        if (!jsonArrayOfEntities.contains(entityForRemovingAsJsonObject))
            throw new IllegalArgumentException("The entity does not exist in the file!");

        jsonArrayOfEntities.remove(entityForRemovingAsJsonObject);

        changeValueOfPropertyInJsonFile(entity, jsonArrayOfEntities, backUpFile);
    }

    // #TODO move logic to question
    public void editExistingQuestion(AbstractQuestion questionForEditing,
                                                AbstractQuestion newQuestion, List<AbstractQuestion> questions, Entities entity) {

        if (checkForDuplicates(questionForEditing, newQuestion, questions) != null)
            throw new IllegalArgumentException("This question already exists!");

        questionForEditing=newQuestion;

        JsonElement questionsAsJsonElement = gson.toJsonTree(questions);
        changeValueOfPropertyInJsonFile(entity, questionsAsJsonElement, true);
    }

    // #TODO move logic to question
    public AbstractQuestion checkForDuplicates(AbstractQuestion questionForEditing,
                                                AbstractQuestion newQuestion, List<AbstractQuestion> questions) {
        for (AbstractQuestion question :
                questions) {
            if (question != questionForEditing && question.equals(newQuestion))
                return question;
        }
        return null;
    }


}
