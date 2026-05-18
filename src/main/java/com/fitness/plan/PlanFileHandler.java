package com.fitness.plan;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.*;
import java.util.ArrayList;
import com.fitness.plan.model.*;

@Component
public class PlanFileHandler {

    @Value("${app.plans.file-path}")
    private String filePath;

@PostConstruct                                                    // run app and create a file if they not in folder
    public void initFile() {
    try {
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (dir !=null && !dir.exists()) dir.mkdirs();
        if (!file.exists())
            file.createNewFile();
    } catch (IOException e){
        System.out.println("Warning " + e.getMessage());
    }
}

public void addPlan(MembershipPlan plan) throws IOException{                                                //create
    String newID = generateId();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
        writer.write(plan.toFileString());
        writer.newLine();
    }
}

    private String generateId() throws IOException {
        int nextNum = 3001 + getAllPlans().size();
        return String.format("PLN-%d", nextNum);
    }


public ArrayList<MembershipPlan> getAllPlans() throws IOException{                                        //read
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

public MembershipPlan getPlanById(String planId) throws IOException{
    for (MembershipPlan p : getAllPlans()) {
        if (p.getPlanID().equals(planId)) return p;
    }
    return null;
}

public ArrayList<MembershipPlan> searchPlans(String keyword) throws IOException{
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

}
