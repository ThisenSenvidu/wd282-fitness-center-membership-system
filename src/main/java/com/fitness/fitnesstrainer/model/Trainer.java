package com.fitness.fitnesstrainer.model;

public abstract class Trainer {

    // Private fields — Encapsulation
    private String trainerId;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private String availability;
    private String trainerType;

    // Static field — shared by all Trainer objects
    private static int trainerCount = 0;

    // No-arg constructor — chains to full constructor (Constructor Chaining)
    public Trainer() {
        this("", "", "", "", "", "", "");
    }

    // Full constructor
    public Trainer(String trainerId, String name, String email, String phone, String specialization, String availability, String trainerType){
        this.trainerId = trainerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.availability = availability;
        this.trainerType = trainerType;
        trainerCount++;
    }

    // Abstract method — subclasses MUST override this (Abstraction + Polymorphism)
    public abstract String getScheduleDescription();

    // Can be overridden by subclasses
    public String getTypeLabel(){
        return "Trainer";
    }

    // Static method
    public static int getTrainerCount(){
        return trainerCount;
    }

    // Convert trainer to one line for saving to file
    public String toFileString(){
        return trainerId + " | " + name + " | " + email + " | " + phone + " | " + specialization + " | " + availability + " | " + trainerType;
    }

    // Read one line from file and return the correct subclass object
    public static Trainer fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;
        if (parts[6].trim().equals("personal")) {
            return new PersonalTrainer(parts[0],parts[1],parts[2],parts[3],parts[4],parts[5]);
        }
        else {
            return new GroupTrainer(parts[0],parts[1],parts[2],parts[3],parts[4],parts[5]);
        }
    }

    // Getters and Setters — Encapsulation
    public String getTrainerId() {
        return trainerId;
    }
    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String s) {
        this.specialization = s;
    }

    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String a) {
        this.availability = a;
    }

    public String getTrainerType() {
        return trainerType;
    }
    public void setTrainerType(String t) {
        this.trainerType = t;
    }

    @Override
    public String toString() {
        return "Trainer[id=" + trainerId + ", name=" + name + "]";
    }
}
