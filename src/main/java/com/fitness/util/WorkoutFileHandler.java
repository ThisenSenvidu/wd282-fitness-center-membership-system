package com.fitness.util;


import com.fitness.session.WorkoutProgram;
import com.fitness.session.StrengthProgram;
import com.fitness.session.CardioProgram;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;


@Component
public class WorkoutFileHandler {


    @Value("${app.workout.file-path}")
    private String filePath;

    // Runs once at startup — creates folder and file if they don't exist
    @PostConstruct
    public void initFile() {
        try {
            File file = new File(filePath);
            File dir  = file.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            if (!file.exists())               file.createNewFile();
        } catch (IOException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    // CREATE — append one new line to programs.txt
    public void addProgram(WorkoutProgram program) throws IOException {
        String newId = generateId();
        program.setProgramId(newId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(program.toFileString());
            writer.newLine();
        }
    }

    // READ ALL — return every program as ArrayList<WorkoutProgram>
    public ArrayList<WorkoutProgram> getAllPrograms() throws IOException {
        ArrayList<WorkoutProgram> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    WorkoutProgram p = WorkoutProgram.fromFileString(line); // Polymorphism
                    if (p != null) list.add(p);
                }
            }
        }
        return list;
    }

    // READ BY ID — find one program by its ID
    public WorkoutProgram getProgramById(String programId) throws IOException {
        for (WorkoutProgram p : getAllPrograms()) {
            if (p.getProgramId().equals(programId)) return p;
        }
        return null;
    }

    // SEARCH — filter by program name or target goal keyword
    public ArrayList<WorkoutProgram> searchPrograms(String keyword) throws IOException {
        ArrayList<WorkoutProgram> results = new ArrayList<>();
        // String manipulation: toLowerCase() + contains()
        String kw = keyword.toLowerCase().trim();
        for (WorkoutProgram p : getAllPrograms()) {
            if (p.getProgramName().toLowerCase().contains(kw) ||
                    p.getTargetGoal().toLowerCase().contains(kw)) {
                results.add(p);
            }
        }
        return results;
    }

    // UPDATE — find matching program, replace it, rewrite the whole file
    public boolean updateProgram(WorkoutProgram updated) throws IOException {
        ArrayList<WorkoutProgram> all = getAllPrograms();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getProgramId().equals(updated.getProgramId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // DELETE — remove program with matching ID, rewrite file
    public boolean deleteProgram(String programId) throws IOException {
        ArrayList<WorkoutProgram> all = getAllPrograms();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getProgramId().equals(programId)) {
                all.remove(i);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // Helper — overwrite entire file with updated list
    private void writeAllToFile(ArrayList<WorkoutProgram> programs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (WorkoutProgram p : programs) {
                writer.write(p.toFileString());
                writer.newLine();
            }
        }
    }

    // Helper — generate ID like WKT-5001, WKT-5002
    // String manipulation: String.format()
    private String generateId() throws IOException {
        int nextNum = 5001 + getAllPrograms().size();
        return String.format("WKT-%d", nextNum);
    }
}
