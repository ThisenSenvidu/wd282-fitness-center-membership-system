package com.fitness.fitnesstrainer.model;


public class BasicPlan extends MembershipPlan {

    public BasicPlan() {
        super();
        setPlanType("basic");
    }

    public BasicPlan(String planId, String planName, double price,
                     int durationMonths, String description) {
        super(planId, planName, price, durationMonths, description, "basic");
    }

    // Polymorphism — BasicPlan returns the base price with no discount
    @Override
    public double calculatePrice() {
        return getPrice();
    }

    @Override
    public String getPlanTypeLabel() {
        return "Basic Plan";
    }
}
