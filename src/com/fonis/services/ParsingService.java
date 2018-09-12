package com.fonis.services;

import com.fonis.entities.Participant;
import com.fonis.entities.Question;
import com.fonis.resources.Resources.EntityType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ParsingService {

    private Gson gson;

    public ParsingService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public List getEntitiesAsList(EntityType entityType, String fileName) {
        Class type = entityType.equals(EntityType.Pariticipant) ? Participant.class : Question.class;
        Type collectionType = TypeToken.getParameterized(List.class, type).getType();
        JsonArray entitiesInJsonArray= getEntitiesAsJsonArray(entityType, fileName);
        List entitiesInList=gson.fromJson(entitiesInJsonArray, collectionType);
        return entitiesInList;
    }


    public JsonArray getEntitiesAsJsonArray(EntityType entityType, String fileName) {
        String propertyName = entityType.toString()+'s';
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















}
