package com.fonis.entities;

public class Participant{
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private int totalPoints;

    public Participant(){
        this.name = "";
        this.surname = "";
        this.phoneNumber = "";
        this.totalPoints = 0;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public int getTotalPoints(){
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints){
        this.totalPoints = totalPoints;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
