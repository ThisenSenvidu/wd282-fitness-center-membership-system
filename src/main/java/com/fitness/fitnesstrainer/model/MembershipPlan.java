package com.fitness.fitnesstrainer.model;


public abstract class MembershipPlan {

    private String planId;
    private String planName;
    private double price;
    private int    durationMonths;
    private String description;
    private String planType;        // "basic" or "premium"

    private static int planCount = 0;

    // No-arg constructor — Constructor Chaining
    public MembershipPlan() {
        this("", "", 0.0, 0, "", "");
    }

    // Full constructor
    public MembershipPlan(String planId, String planName, double price,
                          int durationMonths, String description, String planType) {
        this.planId         = planId;
        this.planName       = planName;
        this.price          = price;
        this.durationMonths = durationMonths;
        this.description    = description;
        this.planType       = planType;
        planCount++;
    }

    // Abstract method — Polymorphism
    // Each subclass calculates its price differently
    public abstract double calculatePrice();

    // Returns a label — overridden by subclasses (Polymorphism)
    public String getPlanTypeLabel() {
        return "Plan";
    }

    // Static method
    public static int getPlanCount() {
        return planCount;
    }

    // Save plan as pipe-separated line for plans.txt
    public String toFileString() {
        return planId + "|" + planName + "|" + price + "|" +
               durationMonths + "|" + description + "|" + planType;
    }

    // Read one line from plans.txt and return correct subclass (Polymorphism)
    public static MembershipPlan fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;
        try {
            String planId         = parts[0].trim();
            String planName       = parts[1].trim();
            double price          = Double.parseDouble(parts[2].trim());
            int    durationMonths = Integer.parseInt(parts[3].trim());
            String description    = parts[4].trim();
            String planType       = parts[5].trim();

            if (planType.equals("premium")) {
                return new PremiumPlan(planId, planName, price, durationMonths, description);
            } else {
                return new BasicPlan(planId, planName, price, durationMonths, description);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Getters and Setters — Encapsulation
    public String getPlanId()                              { return planId; }
    public void   setPlanId(String planId)                 { this.planId = planId; }

    public String getPlanName()                            { return planName; }
    public void   setPlanName(String planName)             { this.planName = planName; }

    public double getPrice()                               { return price; }
    public void   setPrice(double price)                   { this.price = price; }

    public int  getDurationMonths()                        { return durationMonths; }
    public void setDurationMonths(int durationMonths)      { this.durationMonths = durationMonths; }

    public String getDescription()                         { return description; }
    public void   setDescription(String description)       { this.description = description; }

    public String getPlanType()                            { return planType; }
    public void   setPlanType(String planType)             { this.planType = planType; }

    // Formatted price string for display in HTML
    public String getFormattedPrice() {
        return String.format("Rs. %.2f / month", calculatePrice());
    }

    @Override
    public String toString() {
        return "MembershipPlan[id=" + planId + ", name=" + planName + ", type=" + planType + "]";
    }
}
