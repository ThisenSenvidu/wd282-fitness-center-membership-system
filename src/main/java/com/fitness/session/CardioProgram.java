package com.fitness.session;


public class CardioProgram extends WorkoutProgram {

    public CardioProgram() {
        super();
        setProgramType("cardio");
    }

    public CardioProgram(String programId, String programName, String targetGoal,
                         int durationWeeks, String exercises, String difficultyLevel) {
        super(programId, programName, targetGoal, durationWeeks, exercises, difficultyLevel, "cardio");
    }

    // Polymorphism — CardioProgram gives cardio-specific details
    // Different output from StrengthProgram.getProgramDetails()
    @Override
    public String getProgramDetails() {
        return "Cardio Training | Focus: Endurance & Heart Rate | Goal: " + getTargetGoal() + " | Duration: " + getDurationWeeks() + " weeks";
    }

    @Override
    public String getProgramTypeLabel() {
        return "Cardio Training";
    }
}
