package com.fitness.plan;

import com.fitness.plan.model.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class PlanService {

    public final PlanFileHandler fileHandler;

    public PlanService(PlanFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ArrayList<MembershipPlan> getAllPlans() {
        try {
            return fileHandler.getAllPlans();
        } catch (Exception e) {
            throw new RuntimeException("Could not load plans: " + e.getMessage());
        }
    }

    public MembershipPlan getPlanByID(String id) {
        try {
            return fileHandler.getPlanById(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not find plan: " + e.getMessage());
        }
    }

    public ArrayList<MembershipPlan> searchPlans(String keyword) {
        try {
            return fileHandler.searchPlans(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }

    public void addPlan(String planName, double price, int duration, String description, String planType) {
        MembershipPlan plan;
        if ("premium".equals(planType)) {
            plan = new PremiumPlan(duration, null, planName, price, description);
        } else {
            plan = new BasicPlan(null, planName, price, duration, description);
        }
        try {
            fileHandler.addPlan(plan);
        } catch (Exception e) {
            throw new RuntimeException("Could not save plan: " + e.getMessage());
        }
    }

    public boolean updatePlan(String planId, String planName, double price, int duration, String description, String planType) {
        MembershipPlan updated;
        if ("premium".equals(planType)) {
            updated = new PremiumPlan(duration, planId, planName, price, description);
        } else {
            updated = new BasicPlan(planId, planName, price, duration, description);
        }
        try {
            return fileHandler.updatePlan(updated);
        } catch (Exception e) {
            throw new RuntimeException("Could not update plan: " + e.getMessage());
        }
    }

    public boolean deletePlan(String planId) {
        try {
            return fileHandler.deletePlan(planId);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete plan: " + e.getMessage());
        }
    }
}