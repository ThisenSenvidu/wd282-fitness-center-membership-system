package com.fitness.fitnesstrainer.service;

import com.fitness.fitnesstrainer.model.BasicPlan;
import com.fitness.fitnesstrainer.model.MembershipPlan;
import com.fitness.fitnesstrainer.model.PremiumPlan;
import com.fitness.fitnesstrainer.util.PlanFileHandler;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class PlanService {

    private final PlanFileHandler fileHandler;

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

    public MembershipPlan getPlanById(String id) {
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

    // Polymorphism — creates BasicPlan or PremiumPlan based on planType
    public void addPlan(String planName, double price, int durationMonths,
                        String description, String planType) {
        MembershipPlan plan;
        if ("premium".equals(planType)) {
            plan = new PremiumPlan(null, planName, price, durationMonths, description);
        } else {
            plan = new BasicPlan(null, planName, price, durationMonths, description);
        }
        try {
            fileHandler.addPlan(plan);
        } catch (Exception e) {
            throw new RuntimeException("Could not save plan: " + e.getMessage());
        }
    }

    public boolean updatePlan(String planId, String planName, double price,
                               int durationMonths, String description, String planType) {
        MembershipPlan updated;
        if ("premium".equals(planType)) {
            updated = new PremiumPlan(planId, planName, price, durationMonths, description);
        } else {
            updated = new BasicPlan(planId, planName, price, durationMonths, description);
        }
        try {
            return fileHandler.updatePlan(updated);
        } catch (Exception e) {
            throw new RuntimeException("Could not update plan: " + e.getMessage());
        }
    }

    public boolean deletePlan(String id) {
        try {
            return fileHandler.deletePlan(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete plan: " + e.getMessage());
        }
    }
}
