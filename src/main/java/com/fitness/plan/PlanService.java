package com.fitness.plan;

import com.fitness.plan.model.MembershipPlan;

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

}
