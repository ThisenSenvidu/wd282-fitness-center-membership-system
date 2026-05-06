package com.fitness.trainer;

// Inheritance - PersonalTrainer extends Trainer
public class PersonalTrainer extends Trainer {

    private int maxClients;
    private double sessionRate;   // rate per session in Rs.
    private boolean poolCoach;

    public PersonalTrainer() {
        super();
        setTrainerType("personal");
    }

    public PersonalTrainer(String trainerId, String fullName, String email, String phone, String specialisation, String experience, String status, int maxClients, double sessionRate, boolean poolCoach) {
        super(trainerId, fullName, email, phone, specialisation, "personal", experience, status);
        this.maxClients  = maxClients;
        this.sessionRate = sessionRate;
        this.poolCoach   = poolCoach;
    }

    // Getters & Setters
    public int getMaxClients()                 { return maxClients; }
    public void setMaxClients(int maxClients)  { this.maxClients = maxClients; }

    public double getSessionRate()                     { return sessionRate; }
    public void setSessionRate(double sessionRate)     { this.sessionRate = sessionRate; }

    public boolean isPoolCoach()               { return poolCoach; }
    public void setPoolCoach(boolean v)        { this.poolCoach = v; }

    // Polymorphism - Override getTrainerSummary()
    @Override
    public String getTrainerSummary() {
        return "[PERSONAL] " + getFullName()
                + " | " + getSpecialisation()
                + " | Rs." + sessionRate + "/session"
                + " | Max " + maxClients + " clients"
                + (poolCoach ? " | Pool Coach" : "");
    }

    // File handling
    @Override
    public String toFileString() {
        return super.toFileString() + ",PERSONAL,"
                + maxClients + "," + sessionRate + "," + poolCoach;
    }

    public static PersonalTrainer fromPersonalFileString(String line) {
        String[] p = line.split(",", 12);
        if (p.length < 12) return null;
        return new PersonalTrainer(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[6].trim(), p[7].trim(),
                Integer.parseInt(p[9].trim()),
                Double.parseDouble(p[10].trim()),
                Boolean.parseBoolean(p[11].trim())
        );
    }
}