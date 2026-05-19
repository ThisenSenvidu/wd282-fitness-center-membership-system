package com.fitness.fitnesstrainer.model;

/**
 * PremiumMember.java — Subclass of Member.
 *
 * OOP Concepts:
 *   Inheritance       — extends Member
 *   Polymorphism      — overrides getMembershipDetails() and getMemberTypeLabel()
 *   Constructor Chain — calls super() to reuse parent constructor
 */
public class PremiumMember extends Member {

    public PremiumMember() {
        super();
        setMembershipPlan("premium");
    }

    public PremiumMember(String memberId, String name, String email,
                         String phone, String address, String joinDate) {
        super(memberId, name, email, phone, address, "premium", joinDate);
    }

    // Polymorphism — different output from RegularMember
    @Override
    public String getMembershipDetails() {
        return "Premium Plan | Unlimited Access | Personal Trainer Included";
    }

    @Override
    public String getMemberTypeLabel() {
        return "Premium Member";
    }
}
