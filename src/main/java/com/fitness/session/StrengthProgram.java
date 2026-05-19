package com.fitness.session;


public class StrengthProgram extends WorkoutProgram {

    public StrengthProgram() {
        super();
        setProgramType("strength");

    }

    public StrengthProgram(String programId, String programName, String targetGoal,
                           int durationWeeks, String exercises, String difficultyLevel) {
        super(programId, programName, targetGoal,
                durationWeeks, exercises, difficultyLevel, "strength");
    }

    // Polymorphism — StrengthProgram gives strength-specific details
    @Override
    public String getProgramDetails() {
        return "Strength Training | Focus: Sets & Reps | Goal: " + getTargetGoal() +
                " | Duration: " + getDurationWeeks() + " weeks";
    }

    @Override
    public String getProgramTypeLabel() {
        return "Strength Training";
    }
}
