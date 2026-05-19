package com.fitness.fitnesstrainer.model;

// Inheritance
public class GroupTrainer extends Trainer {

    public GroupTrainer() {
        super();
        setTrainerType("group");
    }

    public GroupTrainer(String trainerId, String name, String email, String phone, String specialization, String availability) {
        super(trainerId, name, email, phone, specialization, availability, "group");
    }

    // Polymorphism
    @Override
    public String getScheduleDescription() {
        return "Group Fitness Classes | " + getAvailability();
    }

    @Override
    public String getTypeLabel() {
        return "Group Trainer";
    }
}
