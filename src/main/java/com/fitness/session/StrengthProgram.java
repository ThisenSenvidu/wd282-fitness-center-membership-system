package com.fitness.session;

/**
 * StrengthProgram.java — Subclass of WorkoutProgram for strength training.
 *
 * OOP Concepts:
 *   Inheritance       — extends WorkoutProgram
 *   Polymorphism      — overrides getProgramDetails() and getProgramTypeLabel()
 *                       StrengthProgram focuses on sets, reps and muscle building
 *   Constructor Chain — calls super() to reuse parent constructor
 */
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
