package com.fitness.fitnesstrainer.model;

/**
 * WorkoutProgram.java — Abstract base class for all workout programs.
 *
 * OOP Concepts:
 *   Encapsulation      — private fields with getters/setters
 *   Abstract Class     — cannot be instantiated directly
 *   Abstraction        — abstract method getProgramDetails() forces subclasses
 *                        to describe their own program type
 *   Constructor Chain  — no-arg constructor calls full constructor via this()
 *   Static Modifier    — programCount tracks total objects created
 */
public abstract class WorkoutProgram {

    private String programId;
    private String programName;
    private String targetGoal;       // e.g. "Weight Loss", "Muscle Gain"
    private int    durationWeeks;
    private String exercises;        // comma-separated list of exercises
    private String difficultyLevel;  // "beginner", "intermediate", "advanced"
    private String programType;      // "strength" or "cardio"

    private static int programCount = 0;

    // No-arg constructor — Constructor Chaining
    public WorkoutProgram() {
        this("", "", "", 0, "", "", "");
    }

    // Full constructor
    public WorkoutProgram(String programId, String programName, String targetGoal,
                          int durationWeeks, String exercises,
                          String difficultyLevel, String programType) {
        this.programId       = programId;
        this.programName     = programName;
        this.targetGoal      = targetGoal;
        this.durationWeeks   = durationWeeks;
        this.exercises       = exercises;
        this.difficultyLevel = difficultyLevel;
        this.programType     = programType;
        programCount++;
    }

    // Abstract method — Polymorphism
    // Each subclass must describe its own program details differently
    public abstract String getProgramDetails();

    // Returns a display label — overridden by subclasses (Polymorphism)
    public String getProgramTypeLabel() {
        return "Workout Program";
    }

    // Static method — Static Modifier
    public static int getProgramCount() {
        return programCount;
    }

    // Save program as pipe-separated line for programs.txt
    public String toFileString() {
        return programId + "|" + programName + "|" + targetGoal + "|" +
                durationWeeks + "|" + exercises + "|" + difficultyLevel + "|" + programType;
    }

    // Read one line from programs.txt and return correct subclass (Polymorphism)
    public static WorkoutProgram fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;
        try {
            String programId       = parts[0].trim();
            String programName     = parts[1].trim();
            String targetGoal      = parts[2].trim();
            int    durationWeeks   = Integer.parseInt(parts[3].trim());
            String exercises       = parts[4].trim();
            String difficultyLevel = parts[5].trim();
            String programType     = parts[6].trim();

            if (programType.equals("strength")) {
                return new StrengthProgram(programId, programName, targetGoal,
                        durationWeeks, exercises, difficultyLevel);
            } else {
                return new CardioProgram(programId, programName, targetGoal,
                        durationWeeks, exercises, difficultyLevel);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Getters and Setters — Encapsulation
    public String getProgramId()                           { return programId; }
    public void   setProgramId(String programId)           { this.programId = programId; }

    public String getProgramName()                         { return programName; }
    public void   setProgramName(String programName)       { this.programName = programName; }

    public String getTargetGoal()                          { return targetGoal; }
    public void   setTargetGoal(String targetGoal)         { this.targetGoal = targetGoal; }

    public int  getDurationWeeks()                         { return durationWeeks; }
    public void setDurationWeeks(int durationWeeks)        { this.durationWeeks = durationWeeks; }

    public String getExercises()                           { return exercises; }
    public void   setExercises(String exercises)           { this.exercises = exercises; }

    public String getDifficultyLevel()                     { return difficultyLevel; }
    public void   setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getProgramType()                         { return programType; }
    public void   setProgramType(String programType)       { this.programType = programType; }

    @Override
    public String toString() {
        return "WorkoutProgram[id=" + programId + ", name=" + programName +
                ", type=" + programType + "]";
    }
}