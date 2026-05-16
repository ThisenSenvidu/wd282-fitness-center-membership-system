package com.fitness.fitnesstrainer.util;

import com.fitness.fitnesstrainer.model.Trainer;
import com.fitness.fitnesstrainer.model.PersonalTrainer;
import com.fitness.fitnesstrainer.model.GroupTrainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;

@Component
public class TrainerFileHandler {

    @Value("${app.data.file-path}")
    private String filePath;

    // Runs once at startup — creates the folder and file if they don't exist
    @PostConstruct
    public void initFile() {
        try {
            File file = new File(filePath);
            File dir  = file.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            if (!file.exists())               file.createNewFile();
        }
        catch (IOException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    // CREATE — append one new line to trainers.txt
    public void addTrainer(Trainer trainer) throws IOException {
        String newId = generateId();
        trainer.setTrainerId(newId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(trainer.toFileString());
            writer.newLine();
        }
    }

    // READ — return all trainers as ArrayList
    public ArrayList<Trainer> getAllTrainers() throws IOException {
        ArrayList<Trainer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Trainer t = Trainer.fromFileString(line);
                    if (t != null) list.add(t);
                }
            }
        }
        return list;
    }

    // READ — find one trainer by ID
    public Trainer getTrainerById(String trainerId) throws IOException {
        for (Trainer t : getAllTrainers()) {
            if (t.getTrainerId().equals(trainerId)) return t;
        }
        return null;
    }

    // SEARCH — filter by name or specialization
    public ArrayList<Trainer> searchTrainers(String keyword) throws IOException {
        ArrayList<Trainer> results = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        for (Trainer t : getAllTrainers()) {
            if (t.getName().toLowerCase().contains(kw) ||
                    t.getSpecialization().toLowerCase().contains(kw)) {
                results.add(t);
            }
        }
        return results;
    }

    // UPDATE — find matching trainer, replace it, rewrite file
    public boolean updateTrainer(Trainer updated) throws IOException {
        ArrayList<Trainer> all = getAllTrainers();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getTrainerId().equals(updated.getTrainerId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // DELETE — remove trainer with matching ID, rewrite file
    public boolean deleteTrainer(String trainerId) throws IOException {
        ArrayList<Trainer> all = getAllTrainers();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getTrainerId().equals(trainerId)) {
                all.remove(i);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // Helper — overwrite entire file with new list
    private void writeAllToFile(ArrayList<Trainer> trainers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Trainer t : trainers) {
                writer.write(t.toFileString());
                writer.newLine();
            }
        }
    }

    // Helper — generate ID like TRN-1001, TRN-1002
    private String generateId() throws IOException {
        int nextNum = 1001 + getAllTrainers().size();
        return String.format("TRN-%d", nextNum);
    }
}