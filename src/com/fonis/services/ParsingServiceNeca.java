package com.fonis.services;

import com.fonis.resources.Resources;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// #TODO  Fix saving to JSON files with multiple json objects in them, merge with other parsing services for unique parsing service
public class ParsingServiceNeca{
    private Gson gson;

    public ParsingServiceNeca(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Converts a list of entities into a JSON file.
     * @param entities List of entities to be saved to the $entityName.json file
     * @param entityType Name of the entity
     * @param backupOldFile If set to true, the $entityName.json file will be backed up
     *                      to the backup folder. If set to false, the entities.json file will
     *                      be overwritten with the new one.
     */
    public void parseEntitiesToJson(List<?> entities, Resources.Entities entityType, boolean backupOldFile){
        // Convert list to JSON String
        String entitiesString = this.gson.toJson(entities);

        JsonParser jsonParser = new JsonParser();
        JsonElement entitiesElement = jsonParser.parse(entitiesString);

        this.writeElementToEntitiesJsonFile(entitiesElement, entityType, backupOldFile);
    }

    /**
     * @param type Class which will be used as the list type.
     * @param entityName Name of the entity from which the list will be generated
     * @return List of objects, of entered type, generated from the JSON file.
     */
    public <T> List<T> getEntitiesJsonAsList(Class<T> type, Resources.Entities entityName){
        JsonElement entitiesElement = this.getEntitiesJsonAsElement(entityName);

        // Convert JSON element to list
        Type listType = TypeToken.getParameterized(List.class, type).getType();
        return this.gson.fromJson(entitiesElement, listType);
    }

    /**
     * Adds the entity to the currently active $entityName.json file.
     * @param entity Entity to be added.
     * @param entityType Name of the entity to be added
     * @param backupOldFile If set to true, a backup of the current $entityName.json file will be created.
     */
    public void addEntityToJsonFile(Object entity, Resources.Entities entityType, boolean backupOldFile){
        JsonElement entitiesElement = this.getEntitiesJsonAsElement(entityType);

        // Add new entity
        JsonElement newEntity = this.gson.toJsonTree(entity);
        entitiesElement.getAsJsonArray().add(newEntity);

        this.writeElementToEntitiesJsonFile(entitiesElement, entityType, backupOldFile);
    }

    /**
     * @param entityType Name of the entity
     * @return The $entityName.json file converted into a JsonElement.
     */
    private JsonElement getEntitiesJsonAsElement(Resources.Entities entityType){
        JsonElement entitiesElement = null;

        try(JsonReader jsonReader = new JsonReader(new FileReader("resources/" + entityType.getEntityJsonFileName() + ".json"))){
            JsonObject entitiesObject = this.gson.fromJson(jsonReader, JsonObject.class);
            entitiesElement = entitiesObject.get(entityType.getEntityName());
        }catch(FileNotFoundException e){
            System.out.println("No " + entityType.getEntityJsonFileName() + " json file available.");
        }catch(IOException e){
            e.printStackTrace();
        }
        return entitiesElement;
    }

    private void writeElementToEntitiesJsonFile(JsonElement entities, Resources.Entities entityType, boolean backupOldFile){
        // Backup old file, save to new file
        if(backupOldFile){
            this.backupEntitiesJsonFile(entityType.getEntityJsonFileName());
        }
        try(Writer writer = new FileWriter("resources/" + entityType.getEntityJsonFileName() + ".json")){
            JsonObject entitiesObject = new JsonObject();
            entitiesObject.add(entityType.getEntityName(), entities);
            this.gson.toJson(entitiesObject, writer);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Creates the backup of the currently active $entityName.json file. The backup word is added to the file name
     * as well as the current date time in the dd-MM-yyyy HH-mm-ss format, and the file is placed in the backup folder
     * @param entityName Name of the entity whose file should be backed up
     */
    private void backupEntitiesJsonFile(String entityName){
        File entities = new File("resources/" + entityName + ".json");
        if(entities.exists()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
            String currentDateTime = dateTimeFormatter.format(LocalDateTime.now());
            File backupFile = new File("resources/backup/"+ entityName + "-backup-" + currentDateTime + ".json");
            entities.renameTo(backupFile);
        }
    }
}
