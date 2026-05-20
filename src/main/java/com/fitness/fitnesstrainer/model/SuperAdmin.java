package com.fitness.fitnesstrainer.model;


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