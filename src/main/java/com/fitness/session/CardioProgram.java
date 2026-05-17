package com.fitness.session;

public class CardioProgram extends WorkoutProgram {

    // Default Constructor
    public CardioProgram() {
        super();                    // Call parent default constructor
        setProgramType("cardio");   // Set program type
    }

    // Parameterized Constructor
    public CardioProgram(String programId, String programName, String targetGoal, int durationWeeks, String exercises, String difficultyLevel) {

        // Call parent parameterized constructor
        super(programId, programName, targetGoal, durationWeeks, exercises, difficultyLevel, "cardio");
    }

    @Override
    public String getProgramDetails() {
        return "Cardio Training | Focus: Endurance & Heart Rate | Goal: " + getTargetGoal() + " | Duration: " + getDurationWeeks() + " weeks";
    }

    @Override
    public String getProgramTypeLabel() {

        return "Cardio Training";
    }
}