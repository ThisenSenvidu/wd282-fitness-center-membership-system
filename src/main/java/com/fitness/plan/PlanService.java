package com.fitness.plan;


import com.fitness.plan.model.*;
import java.io.IOException;
import java.util.ArrayList;

public class PlanService {

    public final PlanFileHandler fileHandler;

    public PlanService(PlanFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ArrayList<MembershipPlan> getAllPlans(){
        try {
            return fileHandler.getAllPlans();
        }catch (Exception e){
            throw new RuntimeException("Could not load plans: " + e.getMessage());
        }
    }

    public MembershipPlan getPlanByID(String ID){
        try{
            return fileHandler.getPlanById(ID);
        }catch (Exception e){
            throw new RuntimeException("Could not find plan: " + e.getMessage());
        }
    }
public ArrayList<MembershipPlan> searchPlans(String keyword) {
        try {
            return fileHandler.searchPlans(keyword);
        } catch (IOException e) {
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
}

public boolean updatePlan(String planId, String planName, double price, int duration, String description, String planType) throws IOException {
    MembershipPlan updated;
    if ("premium".equals(planType)) {
        updated = new PremiumPlan(planId, planName, price, duration, description);
    } else {
        updated = new BasicPlan(planId, planName, price, duration, description);
    }
}

}
