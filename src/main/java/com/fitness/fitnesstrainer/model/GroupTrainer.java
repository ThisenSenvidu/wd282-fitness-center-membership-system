package com.fitness.fitnesstrainer.model;

// Inheritance: extends Trainer
public class GroupTrainer extends Trainer {

    public GroupTrainer() {
        super();
        setTrainerType("group");
    }

    public GroupTrainer(String trainerId, String name, String email, String phone, String specialization, String availability) {
        super(trainerId, name, email, phone, specialization, availability, "group");
    }

    // Polymorphism: different output from PersonalTrainer
    @Override
    public String getScheduleDescription() {
        return "Group Fitness Classes | " + getAvailability();
    }

    @Override
    public String getTypeLabel() {
        return "Group Trainer";
    }
}
