package com.fitness.member;

// Inheritance - PremiumMember extends Member
public class PremiumMember extends Member {

    private boolean poolAccess;
    private boolean spaAccess;
    private String nutritionPlan;
    private int personalTrainerSessions;

    public PremiumMember() {
        super();
    }

    public PremiumMember(String memberId, String firstName, String lastName, String dob,
                         String gender, String address, String email, String phone,
                         String emergencyContact, String memberType, String planId,
                         String joinDate, String status,
                         boolean poolAccess, boolean spaAccess,
                         String nutritionPlan, int personalTrainerSessions) {
        super(memberId, firstName, lastName, dob, gender, address, email, phone,
                emergencyContact, memberType, planId, joinDate, status);
        this.poolAccess = poolAccess;
        this.spaAccess = spaAccess;
        this.nutritionPlan = nutritionPlan;
        this.personalTrainerSessions = personalTrainerSessions;
    }

    public boolean isPoolAccess()                          { return poolAccess; }
    public void setPoolAccess(boolean poolAccess)          { this.poolAccess = poolAccess; }

    public boolean isSpaAccess()                           { return spaAccess; }
    public void setSpaAccess(boolean spaAccess)            { this.spaAccess = spaAccess; }

    public String getNutritionPlan()                       { return nutritionPlan; }
    public void setNutritionPlan(String nutritionPlan)     { this.nutritionPlan = nutritionPlan; }

    public int getPersonalTrainerSessions()                        { return personalTrainerSessions; }
    public void setPersonalTrainerSessions(int personalTrainerSessions) { this.personalTrainerSessions = personalTrainerSessions; }

    // Polymorphism - Override getMemberSummary()
    @Override
    public String getMemberSummary() {
        return "[PREMIUM] " + getFullName()
                + " | " + getMemberType()
                + " | " + getPlanId()
                + " | " + getStatus()
                + " | Pool: " + poolAccess
                + " | Spa: " + spaAccess
                + " | PT Sessions: " + personalTrainerSessions;
    }

    // File handling
    @Override
    public String toFileString() {
        return super.toFileString() + ",PREMIUM," + poolAccess + "," + spaAccess + ","
                + nutritionPlan + "," + personalTrainerSessions;
    }

    public static PremiumMember fromPremiumFileString(String line) {
        String[] p = line.split(",", 18);
        if (p.length < 17) return null;
        return new PremiumMember(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[5].replace(";", ",").trim(), p[6].trim(),
                p[7].trim(), p[8].trim(), p[9].trim(), p[10].trim(),
                p[11].trim(), p[12].trim(),
                Boolean.parseBoolean(p[14].trim()),
                Boolean.parseBoolean(p[15].trim()),
                p[16].trim(),
                Integer.parseInt(p[17].trim())
        );
    }
}