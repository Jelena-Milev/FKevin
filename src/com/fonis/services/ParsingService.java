package com.fonis.services;

import com.fonis.entities.Participant;
import com.fonis.entities.Question;
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


    private void changeValueOfPropertyInJsonFile(String propertyName, JsonElement newPropertyValue,
                                                    String fileName) {
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


}
