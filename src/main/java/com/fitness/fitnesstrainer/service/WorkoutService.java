package com.fitness.fitnesstrainer.service;

import com.fitness.fitnesstrainer.model.CardioProgram;
import com.fitness.fitnesstrainer.model.StrengthProgram;
import com.fitness.fitnesstrainer.model.WorkoutProgram;
import com.fitness.fitnesstrainer.util.WorkoutFileHandler;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


@Service
public class WorkoutService {

    private final WorkoutFileHandler fileHandler;

    // Spring automatically provides WorkoutFileHandler here
    public WorkoutService(WorkoutFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ArrayList<WorkoutProgram> getAllPrograms() {
        try {
            return fileHandler.getAllPrograms();
        } catch (Exception e) {
            throw new RuntimeException("Could not load programs: " + e.getMessage());
        }
    }

    public WorkoutProgram getProgramById(String id) {
        try {
            return fileHandler.getProgramById(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not find program: " + e.getMessage());
        }
    }

    public ArrayList<WorkoutProgram> searchPrograms(String keyword) {
        try {
            return fileHandler.searchPrograms(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }

    // Polymorphism — creates StrengthProgram or CardioProgram based on programType
    public void addProgram(String programName, String targetGoal, int durationWeeks,
                           String exercises, String difficultyLevel, String programType) {
        WorkoutProgram program;
        if ("strength".equals(programType)) {
            program = new StrengthProgram(null, programName, targetGoal,
                                          durationWeeks, exercises, difficultyLevel);
        } else {
            program = new CardioProgram(null, programName, targetGoal,
                                        durationWeeks, exercises, difficultyLevel);
        }
        try {
            fileHandler.addProgram(program);
        } catch (Exception e) {
            throw new RuntimeException("Could not save program: " + e.getMessage());
        }
    }

    public boolean updateProgram(String programId, String programName, String targetGoal,
                                  int durationWeeks, String exercises,
                                  String difficultyLevel, String programType) {
        WorkoutProgram updated;
        if ("strength".equals(programType)) {
            updated = new StrengthProgram(programId, programName, targetGoal,
                                          durationWeeks, exercises, difficultyLevel);
        } else {
            updated = new CardioProgram(programId, programName, targetGoal,
                                        durationWeeks, exercises, difficultyLevel);
        }
        try {
            return fileHandler.updateProgram(updated);
        } catch (Exception e) {
            throw new RuntimeException("Could not update program: " + e.getMessage());
        }
    }

    public boolean deleteProgram(String id) {
        try {
            return fileHandler.deleteProgram(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete program: " + e.getMessage());
        }
    }
}
