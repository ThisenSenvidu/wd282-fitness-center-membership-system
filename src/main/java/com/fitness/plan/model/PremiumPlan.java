package com.fitness.plan.model;

public class PremiumPlan extends MembershipPlan {

    private static final double DISCOUNT_RATE = 0.10;


    public PremiumPlan() {
        super();
        setPlanType("premium");
    }


    public PremiumPlan(int duration, String planId, String planName, String planType, String planVersion, double price, String description) {
        super(duration, planId, planName, planType, planVersion, price, description);
    }

    public PremiumPlan(int duration, String planId, String planName, double price, String description) {
        super(duration, planId, planName, "premium", "", price, description);
    }

    @Override
    public double calculatePrice() {
        return getPrice() * (1 - DISCOUNT_RATE);
    }

    @Override
    public String getPlanTypeLabel() {
        return "Premium Plan";
    }
}