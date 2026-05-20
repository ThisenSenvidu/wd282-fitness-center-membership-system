package com.fitness.fitnesstrainer.model;


public class RegularAdmin extends Admin {

    public RegularAdmin() {
        super();
        setAdminType("admin");
    }

    public RegularAdmin(String adminId, String username, String password,
                        String fullName, String email, String createdDate) {
        super(adminId, username, password, fullName, email, "admin", createdDate);
    }

    // Polymorphism — RegularAdmin has limited permissions
    // Different output from SuperAdmin.getAdminPermissions()
    @Override
    public String getAdminPermissions() {
        return "Limited Access: View Members, Trainers, Payments (cannot manage other Admins)";
    }

    @Override
    public String getAdminTypeLabel() {
        return "Admin";
    }
}