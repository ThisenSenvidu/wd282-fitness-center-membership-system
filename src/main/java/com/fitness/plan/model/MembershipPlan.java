package com.fitness.plan.model;

public abstract class MembershipPlan {

    private String planId;
    private String planName;
    private String planType;
    private double price;
    private String planVersion;
    private int durationMonths;
    private String description;
    private static int planCount = 0;

    public MembershipPlan(){
        this(0, "","","","",0,"");
    }

    public MembershipPlan(int duration, String planId, String planName, String planType, String planVersion, double price, String description) {
        this.durationMonths = duration;
        this.planId = planId;
        this.planName = planName;
        this.planType = planType;
        this.planVersion = planVersion;
        this.price = price;
        this.description = description;
        planCount++;
    }


    //Getters & Setters


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public static void setPlanCount(int planCount) {
        MembershipPlan.planCount = planCount;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanID(String planID) {
        this.planId = planID;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanVersion() {
        return planVersion;
    }

    public void setPlanVersion(String planVersion) {
        this.planVersion = planVersion;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }




    // ===== Polymorphism =====
    public abstract double calculatePrice();

    public String getPlanTypeLabel() {
        return "Plan";
    }

    // ===== Static =====
    public static int getPlanCount() {
        return planCount;
    }


    // ===== File Handling =====
    public String toFileString() {
        return planId + "|" + planName + "|" + price + "|" + durationMonths + "|" + description + "|" + planType;
    }

    public static MembershipPlan fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;
        try {
            String planID = parts[0].trim();
            String planName = parts[1].trim();
            double price = Double.parseDouble(parts[2].trim());
            int duration = Integer.parseInt(parts[3].trim());
            String description = parts[4].trim();
            String planType = parts[5].trim();

            if (planType.equals("premium")) {
                return new PremiumPlan( duration,planID, planName, price, description);
            } else {
                return new BasicPlan(planID, planName, price, duration, description);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
        //Display
        public String getFormattedPrice() {
            return String.format("Rs. %.2f / month", calculatePrice());
        }

        @Override
    public String  toString(){
        return "MembershipPlan[id=" + planId + ", name=" + planName + ", type=" + planType + "]";
        }
    }
