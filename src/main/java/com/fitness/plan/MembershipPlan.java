package com.fitness.plan;

public class MembershipPlan {

     private String planID;
     private String planName;
     private String planType;
     private String planVersion;
     private int duration;
     private double price;
     private String status;

    public MembershipPlan(int duration, String planID, String planName, String planType, String planVersion, double price, String status) {
        this.duration = duration;
        this.planID = planID;
        this.planName = planName;
        this.planType = planType;
        this.planVersion = planVersion;
        this.price = price;
        this.status = status;
    }

    public MembershipPlan(String planID, String planName, String planType, String planVersion) {
        this.planID = planID;
        this.planName = planName;
        this.planType = planType;
        this.planVersion = planVersion;
    }

    public MembershipPlan(String planID, String planName, String planType) {
         this.planID = planID;
         this.planName = planName;
         this.planType = planType;
     }

     public String getPlanID() {
         return planID;
     }


    public void setPlanVersion(String planVersion) {
        this.planVersion = planVersion;
    }

    public void setPlanID(String planID) {
         this.planID = planID;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double  price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}