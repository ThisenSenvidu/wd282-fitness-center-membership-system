package com.fitness.plan;

public class BasicPlan extends MembershipPlan {

    public BasicPlan(int duration, String planID, String planName, String planType, String planVersion, double price, String description) {
        super(duration, planID, planName, planType, planVersion, price, description);
    }

    @Override
    public double calculatePrice() {
        return getPrice();
    }

    @Override
    public String getPlanTypeLabel() {
        return "Basic Plan";
    }
}
