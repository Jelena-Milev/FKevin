package com.fonis.services;

import com.fonis.resources.Resources;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// #TODO Fix saving to JSON files with multiple json objects in them
// #TODO Merge with other parsing services for unique parsing service
public class ParsingServiceNeca{
    private final Gson gson;

    public ParsingServiceNeca(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Converts a list of entities into a JSON file.
     * @param entities List of entities to be saved to the entity json file
     * @param entityType The type of entity
     * @param backupFile If set to true, the entity json file will be backed up
     *                      to the backup folder. If set to false, the entity json file will
     *                      be overwritten with the new one.
     */
    public void parseEntitiesToJson(List<?> entities, Resources.Entities entityType, boolean backupFile){
        JsonElement entitiesElement = this.gson.toJsonTree(entities);

        this.writeElementToEntitiesJsonFile(entitiesElement, entityType, backupFile);
    }

    /**
     * @param entityType The type of entity
     * @return List of objects, of entered entity type, generated from entity json file.
     */
    public <T> List<T> getEntitiesJsonAsList(Resources.Entities entityType){
        JsonElement entitiesElement = this.getEntitiesJsonAsElement(entityType);

        // Convert JSON element to list
        Type listType = TypeToken.getParameterized(List.class, entityType.getEntityClass()).getType();
        return this.gson.fromJson(entitiesElement, listType);
    }

    /**
     * Adds the entity to the currently active entity json file.
     * @param entity Entity to be added.
     * @param entityType Type of entity
     * @param backupFile If set to true, a backup of the current entity json file will be created.
     */
    public void addEntityToJsonFile(Object entity, Resources.Entities entityType, boolean backupFile){
        JsonElement entitiesElement = this.getEntitiesJsonAsElement(entityType);

        // Add new entity
        JsonElement newEntity = this.gson.toJsonTree(entity);
        if(entitiesElement == null){
            entitiesElement = newEntity;
        }else if(!entitiesElement.getAsJsonArray().contains(newEntity)){
            entitiesElement.getAsJsonArray().add(newEntity);
        }

        this.writeElementToEntitiesJsonFile(entitiesElement, entityType, backupFile);
    }

    /**
     * @param entityType Type of entity
     * @return The entity json file converted into a JsonElement.
     */
    private JsonElement getEntitiesJsonAsElement(Resources.Entities entityType){
        JsonObject jsonFileContent = this.getJsonFileAsObject(entityType);

        return jsonFileContent.get(entityType.getEntityName());
    }

    private JsonObject getJsonFileAsObject(Resources.Entities entityType){
        JsonObject jsonFileContent = null;

        try(JsonReader jsonReader = new JsonReader(new FileReader(Resources.DATA_LOCATION + entityType.getEntityJsonFileName()))){
            jsonFileContent = this.gson.fromJson(jsonReader, JsonObject.class);
        }catch(FileNotFoundException e){
            System.out.println("No " + entityType.getEntityJsonFileName() + " file available.");
        }catch(IOException e){
            e.printStackTrace();
        }
        return jsonFileContent;
    }

    private void writeElementToEntitiesJsonFile(JsonElement entities, Resources.Entities entityType, boolean backupFile){
        // Backup old file, save to new file
        if(backupFile){
            this.backupEntitiesJsonFile(entityType);
        }

        JsonObject jsonFileContent = this.getJsonFileAsObject(entityType);
        if(jsonFileContent == null){
            jsonFileContent = new JsonObject();
        }

        if(jsonFileContent.has(entityType.getEntityName())){
            jsonFileContent.remove(entityType.getEntityName());
        }

        jsonFileContent.add(entityType.getEntityName(), entities);

        try(FileWriter writer = new FileWriter(Resources.DATA_LOCATION + entityType.getEntityJsonFileName())){
            this.gson.toJson(jsonFileContent, writer);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void removeEntityFromJsonFile(Object entityForRemoving, Resources.Entities entityType, boolean backupFile){
        JsonElement entityForRemovingAsJsonElement = this.gson.toJsonTree(entityForRemoving);
        JsonElement entities = this.getEntitiesJsonAsElement(entityType);

        if(entities == null || entities.getAsJsonArray().size() == 0 || !entities.getAsJsonArray().contains(entityForRemovingAsJsonElement)){
            throw new IllegalStateException("Entity not found in json file");
        }

        entities.getAsJsonArray().remove(entityForRemovingAsJsonElement);
        this.writeElementToEntitiesJsonFile(entities, entityType, backupFile);
    }

    /**
     * Creates the backup of the currently active $entityName.json file. The backup word is added to the file name
     * as well as the current date time in the dd-MM-yyyy HH-mm-ss format, and the file is placed in the backup folder
     * @param entityType Name of the entity whose file should be backed up
     */
    private void backupEntitiesJsonFile(Resources.Entities entityType){
        File originalFile = new File(Resources.DATA_LOCATION + entityType.getEntityJsonFileName());
        if(originalFile.exists()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
            String currentDateTime = dateTimeFormatter.format(LocalDateTime.now());
            File backupFile = new File(Resources.DATA_BACKUP_LOCATION + entityType.getEntityName() + "-backup-" + currentDateTime + ".json");
            try{
                Files.copy(originalFile.toPath(), backupFile.toPath());
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
