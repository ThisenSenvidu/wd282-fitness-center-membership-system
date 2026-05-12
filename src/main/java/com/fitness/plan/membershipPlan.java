package com.fitness.plan;

public class membershipPlan {

     private String planID;
     private String planName;
     private String planType;

     public membershipPlan(String planID, String planName, String planType) {
         this.planID = planID;
         this.planName = planName;
         this.planType = planType;
     }

     public String getPlanID() {
         return planID;
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
 }