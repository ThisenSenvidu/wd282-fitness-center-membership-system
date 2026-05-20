package com.fitness.fitnesstrainer.model;

// Inheritance: extends Trainer
public class PersonalTrainer extends Trainer {

    // No-arg constructor — chains to parent via super()
    public PersonalTrainer() {
        super();
        setTrainerType("personal");
    }

    // Full constructor — Constructor Chaining with super()
    public PersonalTrainer(String trainerId, String name, String email,
                           String phone, String specialization, String availability) {
        super(trainerId, name, email, phone, specialization, availability, "personal");
    }

    // Polymorphism: overrides the abstract method from Trainer
    @Override
    public String getScheduleDescription() {
        return "1-on-1 Personal Sessions | " + getAvailability();
    }

    @Override
    public String getTypeLabel() {
        return "Personal Trainer";
    }
}