package com.fitness.plan;

public class PremiumPlan extends membershipPlan {

    private boolean personalTrainer;
    private boolean accessPool;
    private boolean nutritionAdvice;

    public PremiumPlan(String duration, String planID, String planName, String planType, String planVersion, String price, String status, boolean accessPool, boolean nutritionAdvice, boolean personalTrainer) {
        super(duration, planID, planName, planType, planVersion, price, status);
        this.accessPool = accessPool;
        this.nutritionAdvice = nutritionAdvice;
        this.personalTrainer = personalTrainer;
    }

    public boolean isAccessPool() {
        return accessPool;
    }

    public void setAccessPool(boolean accessPool) {
        this.accessPool = accessPool;
    }

    public boolean isNutritionAdvice() {
        return nutritionAdvice;
    }

    public void setNutritionAdvice(boolean nutritionAdvice) {
        this.nutritionAdvice = nutritionAdvice;
    }

    public boolean isPersonalTrainer() {
        return personalTrainer;
    }

    public void setPersonalTrainer(boolean personalTrainer) {
        this.personalTrainer = personalTrainer;
    }
}
