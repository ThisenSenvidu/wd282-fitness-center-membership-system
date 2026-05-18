package com.fitness.plan;

public class BasicPlan extends MembershipPlan {

    public BasicPlan(int duration, String planID, String planName, String planType, String planVersion, double price, String description) {
        super(duration, planID, planName, planType, planVersion, price, description);
    }

    public BasicPlan(String planID, String planName, double price, String planVersion, int duration,String description){
        super(duration, planID, planName,"Basic", "", price, description);

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
