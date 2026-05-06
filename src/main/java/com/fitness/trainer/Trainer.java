package com.fitness.trainer;

public class Trainer {

    private String trainerId;
    private String fullName;
    private String email;
    private String phone;
    private String specialisation;  // Cardio, Strength, Yoga, etc.
    private String trainerType;     // personal / group
    private String experience;      // years
    private String status;          // active / inactive

    public Trainer() {}

    public Trainer(String trainerId, String fullName, String email, String phone,
                   String specialisation, String trainerType, String experience, String status) {
        this.trainerId      = trainerId;
        this.fullName       = fullName;
        this.email          = email;
        this.phone          = phone;
        this.specialisation = specialisation;
        this.trainerType    = trainerType;
        this.experience     = experience;
        this.status         = status;
    }

    // Getters & Setters (Encapsulation)
    public String getTrainerId()                   { return trainerId; }
    public void setTrainerId(String trainerId)     { this.trainerId = trainerId; }

    public String getFullName()                    { return fullName; }
    public void setFullName(String fullName)       { this.fullName = fullName; }

    public String getEmail()                       { return email; }
    public void setEmail(String email)             { this.email = email; }

    public String getPhone()                       { return phone; }
    public void setPhone(String phone)             { this.phone = phone; }

    public String getSpecialisation()                          { return specialisation; }
    public void setSpecialisation(String specialisation)       { this.specialisation = specialisation; }

    public String getTrainerType()                     { return trainerType; }
    public void setTrainerType(String trainerType)     { this.trainerType = trainerType; }

    public String getExperience()                      { return experience; }
    public void setExperience(String experience)       { this.experience = experience; }

    public String getStatus()                  { return status; }
    public void setStatus(String status)       { this.status = status; }

    // Polymorphism - subclasses override
    public String getTrainerSummary() {
        return fullName + " | " + specialisation + " | " + experience + " yrs";
    }

    // File handling - serialize
    public String toFileString() {
        return trainerId + "," + fullName + "," + email + "," + phone + ","
                + specialisation + "," + trainerType + "," + experience + "," + status;
    }

    // File handling - deserialize
    public static Trainer fromFileString(String line) {
        String[] p = line.split(",", 8);
        if (p.length < 8) return null;
        return new Trainer(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim()
        );
    }
}