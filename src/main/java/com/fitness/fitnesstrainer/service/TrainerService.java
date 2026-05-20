package com.fitness.fitnesstrainer.service;

import com.fitness.fitnesstrainer.model.*;
import com.fitness.fitnesstrainer.util.TrainerFileHandler;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class TrainerService {

    private final TrainerFileHandler fileHandler;

    // Spring automatically provides the TrainerFileHandler bean here
    public TrainerService(TrainerFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ArrayList<Trainer> getAllTrainers() {
        try {
            return fileHandler.getAllTrainers();
        }
        catch (Exception e) {
            throw new RuntimeException("Could not load trainers: " + e.getMessage());
        }
    }

    public Trainer getTrainerById(String id) {
        try {
            return fileHandler.getTrainerById(id);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not find trainer: " + e.getMessage());
        }
    }

    public ArrayList<Trainer> searchTrainers(String keyword) {
        try {
            return fileHandler.searchTrainers(keyword);
        }
        catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }

    // Polymorphism: picks the right subclass based on trainerType
    public void addTrainer(String name, String email, String phone, String specialization, String availability, String trainerType) {
        Trainer trainer;

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }

        if (!email.contains("@")) {
            throw new RuntimeException("Invalid email format");
        }

        if (phone.length() != 10) {
            throw new RuntimeException("Phone must be 10 digits");
        }

        if ("personal".equals(trainerType)) {
            trainer = new PersonalTrainer(null,name.trim(),email.trim(),phone.trim(),specialization.trim(),availability.trim());
        }
        else {
            trainer = new GroupTrainer(null,name.trim(),email.trim(),phone.trim(),specialization.trim(),availability.trim());
        }


        try {
            fileHandler.addTrainer(trainer);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not save trainer: " + e.getMessage());
        }
    }

    public boolean updateTrainer(String trainerId, String name, String email, String phone, String specialization, String availability, String trainerType) {
        Trainer updated;
        if ("personal".equals(trainerType)) {
            updated = new PersonalTrainer(trainerId,name.trim(),email.trim(),phone.trim(),specialization.trim(),availability.trim());
        }
        else {
            updated = new GroupTrainer(trainerId,name.trim(),email.trim(),phone.trim(),specialization.trim(),availability.trim());
        }
        try {
            return fileHandler.updateTrainer(updated);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not update trainer: " + e.getMessage());
        }
    }

    public boolean deleteTrainer(String id) {
        try {
            return fileHandler.deleteTrainer(id);
        }
        catch (Exception e) {
            throw new RuntimeException("Could not delete trainer: " + e.getMessage());
        }
    }
}

