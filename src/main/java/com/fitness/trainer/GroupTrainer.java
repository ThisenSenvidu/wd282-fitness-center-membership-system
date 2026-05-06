package com.fitness.trainer;

public class GroupTrainer extends Trainer {

    private int maxGroupSize;       // max participants per class
    private String classSchedule;  // e.g. "Mon/Wed/Fri 6AM"
    private String venue;          // e.g. "Main Hall", "Outdoor Court"
    private double classRate;      // rate per class in Rs.

    public GroupTrainer() {
        super();
        setTrainerType("group");
    }

    public GroupTrainer(String trainerId, String fullName, String email, String phone,
                        String specialisation, String experience, String status,
                        int maxGroupSize, String classSchedule, String venue, double classRate) {
        super(trainerId, fullName, email, phone, specialisation, "group", experience, status);
        this.maxGroupSize  = maxGroupSize;
        this.classSchedule = classSchedule;
        this.venue         = venue;
        this.classRate     = classRate;
    }

    // Getters & Setters
    public int getMaxGroupSize()                       { return maxGroupSize; }
    public void setMaxGroupSize(int maxGroupSize)      { this.maxGroupSize = maxGroupSize; }

    public String getClassSchedule()                       { return classSchedule; }
    public void setClassSchedule(String classSchedule)     { this.classSchedule = classSchedule; }

    public String getVenue()                   { return venue; }
    public void setVenue(String venue)         { this.venue = venue; }

    public double getClassRate()                   { return classRate; }
    public void setClassRate(double classRate)     { this.classRate = classRate; }

    // Polymorphism - Override getTrainerSummary()
    @Override
    public String getTrainerSummary() {
        return "[GROUP] " + getFullName()
                + " | " + getSpecialisation()
                + " | Rs." + classRate + "/class"
                + " | Max " + maxGroupSize + " participants"
                + " | " + classSchedule
                + " @ " + venue;
    }

    // File handling - serialize
    @Override
    public String toFileString() {
        return super.toFileString() + ",GROUP,"
                + maxGroupSize + "," + classSchedule + "," + venue + "," + classRate;
    }

    // File handling - deserialize
    public static GroupTrainer fromGroupFileString(String line) {
        String[] p = line.split(",", 13);
        if (p.length < 13) return null;
        return new GroupTrainer(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[6].trim(), p[7].trim(),
                Integer.parseInt(p[9].trim()),
                p[10].trim(),
                p[11].trim(),
                Double.parseDouble(p[12].trim())
        );
    }
}