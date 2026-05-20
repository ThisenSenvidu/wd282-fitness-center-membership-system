package com.fitness.fitnesstrainer.model;

/**
 * RegularMember.java — Subclass of Member.
 *
 * OOP Concepts:
 *   Inheritance       — extends Member
 *   Polymorphism      — overrides getMembershipDetails() and getMemberTypeLabel()
 *   Constructor Chain — calls super() to reuse parent constructor
 */
public class RegularMember extends Member {

    public RegularMember() {
        super();
        setMembershipPlan("regular");
    }

    public RegularMember(String memberId, String name, String email,
                         String phone, String address, String joinDate) {
        super(memberId, name, email, phone, address, "regular", joinDate);
    }

    // Polymorphism — different output from PremiumMember
    @Override
    public String getMembershipDetails() {
        return "Regular Plan | Standard Gym Access | Group Classes";
    }

    @Override
    public String getMemberTypeLabel() {
        return "Regular Member";
    }
}
