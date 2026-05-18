package com.fitness.plan;

public class PremiumPlan extends MembershipPlan {

    private static final double DISCOUNT_RATE = 0.10;


    public PremiumPlan() {
        super();
        setPlanType("premium");
    }


    public PremiumPlan(int duration, String planID, String planName, String planType, String planVersion, double price, String description) {
        super(duration, planID, planName, planType, planVersion, price, description);
    }

    public PremiumPlan(int duration, String planID, String planName, double price, String description) {
        super(duration, planID, planName, "premium", "", price, description);
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