package com.fitness.fitnesstrainer.model;


public class PremiumPlan extends MembershipPlan {

    // Discount rate applied to premium plans
    private static final double DISCOUNT_RATE = 0.10;

    public PremiumPlan() {
        super();
        setPlanType("premium");
    }

    public PremiumPlan(String planId, String planName, double price,
                       int durationMonths, String description) {
        super(planId, planName, price, durationMonths, description, "premium");
    }

    // Polymorphism — PremiumPlan applies a 10% discount, different from BasicPlan
    @Override
    public double calculatePrice() {
        return getPrice() * (1 - DISCOUNT_RATE);
    }

    @Override
    public String getPlanTypeLabel() {
        return "Premium Plan";
    }
}
