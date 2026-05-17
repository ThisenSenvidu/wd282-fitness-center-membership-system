package com.fitness.plan;

/**
 * BasicPlan.java — Subclass of MembershipPlan for basic plans.
 *
 * OOP Concepts:
 *   Inheritance       — extends MembershipPlan
 *   Polymorphism      — overrides calculatePrice() and getPlanTypeLabel()
 *   Constructor Chain — calls super() to reuse parent constructor
 */
public class BasicPlan extends MembershipPlan {

    public BasicPlan(int duration, String planID, String planName, String planType, String planVersion, double price, String description) {
        super(duration, planID, planName, planType, planVersion, price, description);
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
