package com.fitness.fitnesstrainer.util;

import com.fitness.fitnesstrainer.model.BasicPlan;
import com.fitness.fitnesstrainer.model.MembershipPlan;
import com.fitness.fitnesstrainer.model.PremiumPlan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;


@Component
public class PlanFileHandler {

    @Value("${app.plans.file-path}")
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

    // CREATE
    public void addPlan(MembershipPlan plan) throws IOException {
        String newId = generateId();
        plan.setPlanId(newId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(plan.toFileString());
            writer.newLine();
        }
    }

    // READ ALL
    public ArrayList<MembershipPlan> getAllPlans() throws IOException {
        ArrayList<MembershipPlan> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    MembershipPlan p = MembershipPlan.fromFileString(line);
                    if (p != null) list.add(p);
                }
            }
        }
        return list;
    }

    // READ BY ID
    public MembershipPlan getPlanById(String planId) throws IOException {
        for (MembershipPlan p : getAllPlans()) {
            if (p.getPlanId().equals(planId)) return p;
        }
        return null;
    }

    // SEARCH by name or description
    public ArrayList<MembershipPlan> searchPlans(String keyword) throws IOException {
        ArrayList<MembershipPlan> results = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        for (MembershipPlan p : getAllPlans()) {
            if (p.getPlanName().toLowerCase().contains(kw) ||
                p.getDescription().toLowerCase().contains(kw)) {
                results.add(p);
            }
        }
        return results;
    }

    // UPDATE
    public boolean updatePlan(MembershipPlan updated) throws IOException {
        ArrayList<MembershipPlan> all = getAllPlans();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPlanId().equals(updated.getPlanId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // DELETE
    public boolean deletePlan(String planId) throws IOException {
        ArrayList<MembershipPlan> all = getAllPlans();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPlanId().equals(planId)) {
                all.remove(i);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // Helper — overwrite entire file with updated list
    private void writeAllToFile(ArrayList<MembershipPlan> plans) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (MembershipPlan p : plans) {
                writer.write(p.toFileString());
                writer.newLine();
            }
        }
    }

    // Helper — generate ID like PLN-3001, PLN-3002
    private String generateId() throws IOException {
        int nextNum = 3001 + getAllPlans().size();
        return String.format("PLN-%d", nextNum);
    }
}
