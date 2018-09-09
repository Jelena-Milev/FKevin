package com.fonis.services;

import com.fonis.entities.Participant;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// #TODO  -convert most of the functions to be usable for any type, set dynamic paths not just participant and merge this with the other ParsingService
public class ParsingServiceNeca{
    private Gson gson;

    public ParsingServiceNeca(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Converts a list of participants into a JSON file.
     * @param participants List of participants to be saved to the participants.json file
     * @param backupOldFile If set to true, the participants.json file will be backed up
     *                      to the backup folder. If set to false, the participants.json file will
     *                      be overwritten with the new one.
     */
    public void parseParticipantsToJson(List<Participant> participants, boolean backupOldFile){
        // Convert list to JSON String
        String participantsString = this.gson.toJson(participants);

        JsonParser jsonParser = new JsonParser();
        JsonElement participantsElement = jsonParser.parse(participantsString);

        this.writeElementToParticipantsJsonFile(participantsElement, backupOldFile);
    }

    /**
     * @param type Class which will be used as the list type.
     * @return List of objects, of entered type, generated from the JSON file.
     */
    public <T> List<T> getParticipantsJsonAsList(Class<T> type){
        JsonElement participantsElement = this.getParticipantsJsonAsElement();

        // Convert JSON element to list
        Type listType = TypeToken.getParameterized(List.class, type).getType();
        return this.gson.fromJson(participantsElement, listType);
    }

    /**
     * Adds the participant to the currently active participants.json file.
     * @param participant Participant to be added.
     * @param backupOldFile If set to true, a backup of the current participants.json file will be created.
     */
    public void addParticipantToJsonFile(Participant participant, boolean backupOldFile){
        JsonElement participantsElement = this.getParticipantsJsonAsElement();

        // Add new participant
        JsonElement newParticipant = this.gson.toJsonTree(participant);
        participantsElement.getAsJsonArray().add(newParticipant);

        this.writeElementToParticipantsJsonFile(participantsElement, backupOldFile);
    }

    /**
     * @return The participants.json file converted into a JsonElement.
     */
    private JsonElement getParticipantsJsonAsElement(){
        JsonElement participantsElement = null;

        try(JsonReader jsonReader = new JsonReader(new FileReader("resources/participants.json"))){
            JsonObject participantsObject = this.gson.fromJson(jsonReader, JsonObject.class);
            participantsElement = participantsObject.get("Participants");
        }catch(FileNotFoundException e){
            System.out.println("No participants json file available");
        }catch(IOException e){
            e.printStackTrace();
        }
        return participantsElement;
    }

    // #TODO rewrite to be reusable for other types
    private void writeElementToParticipantsJsonFile(JsonElement participants, boolean backupOldFile){
        // Backup old file, save to new file
        if(backupOldFile){
            this.backupParticipantsJsonFile();
        }
        try(Writer writer = new FileWriter("resources/participants.json")){
            JsonObject participantsObject = new JsonObject();
            participantsObject.add("Participants", participants);
            this.gson.toJson(participantsObject, writer);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Creates the backup of the currently active participants.json file. The backup word is added to the file name
     * as well as the current date time in the dd-MM-yyyy HH-mm-ss format, and the file is placed in the backup folder
     */
    private void backupParticipantsJsonFile(){
        File participants = new File("resources/participants.json");
        if(participants.exists()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
            String currentDateTime = dateTimeFormatter.format(LocalDateTime.now());
            File backupFile = new File("resources/backup/participants-backup-" + currentDateTime + ".json");
            participants.renameTo(backupFile);
        }
    }
}
