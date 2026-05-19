package com.fitness.fitnesstrainer.model;

/**
 * SuperAdmin.java — Subclass of Admin with full system access.
 *
 * OOP Concepts:
 *   Inheritance       — extends Admin
 *   Polymorphism      — overrides getAdminPermissions() and getAdminTypeLabel()
 *                       SuperAdmin has full access to everything
 *   Constructor Chain — calls super() to reuse parent constructor
 */
public class SuperAdmin extends Admin {

    public SuperAdmin() {
        super();
        setAdminType("superadmin");
    }

    public SuperAdmin(String adminId, String username, String password,
                      String fullName, String email, String createdDate) {
        super(adminId, username, password, fullName, email, "superadmin", createdDate);
    }

    // Polymorphism — SuperAdmin has all permissions
    @Override
    public String getAdminPermissions() {
        return "Full Access: Manage Members, Trainers, Payments, Plans, Workouts, Admins";
    }

    @Override
    public String getAdminTypeLabel() {
        return "Super Admin";
    }
}