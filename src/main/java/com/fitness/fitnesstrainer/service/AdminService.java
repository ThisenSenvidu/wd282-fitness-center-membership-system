package com.fitness.fitnesstrainer.service;

import com.fitness.fitnesstrainer.model.Admin;
import com.fitness.fitnesstrainer.model.SuperAdmin;
import com.fitness.fitnesstrainer.model.RegularAdmin;
import com.fitness.fitnesstrainer.util.AdminFileHandler;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

/**
 * AdminService.java — Business logic layer for Admin Management.
 *
 * Spring Boot concept:
 *   @Service — Spring manages this as a bean; injected into AdminController
 *
 * OOP concepts:
 *   Encapsulation — hides file-handling details from the controller
 *   Polymorphism  — builds SuperAdmin or RegularAdmin based on adminType
 */
@Service
public class AdminService {

    private final AdminFileHandler fileHandler;

    public AdminService(AdminFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // LOGIN — validates username and password
    public Admin login(String username, String password) {
        try {
            return fileHandler.findByUsernameAndPassword(username, password);
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public ArrayList<Admin> getAllAdmins() {
        try {
            return fileHandler.getAllAdmins();
        } catch (Exception e) {
            throw new RuntimeException("Could not load admins: " + e.getMessage());
        }
    }

    public Admin getAdminById(String id) {
        try {
            return fileHandler.getAdminById(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not find admin: " + e.getMessage());
        }
    }

    public ArrayList<Admin> searchAdmins(String keyword) {
        try {
            return fileHandler.searchAdmins(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }

    // Polymorphism — creates SuperAdmin or RegularAdmin based on adminType
    public void addAdmin(String username, String password, String fullName,
                         String email, String adminType, String createdDate) {
        Admin admin;
        if ("superadmin".equals(adminType)) {
            admin = new SuperAdmin(null, username, password, fullName, email, createdDate);
        } else {
            admin = new RegularAdmin(null, username, password, fullName, email, createdDate);
        }
        try {
            fileHandler.addAdmin(admin);
        } catch (Exception e) {
            throw new RuntimeException("Could not save admin: " + e.getMessage());
        }
    }

    public boolean updateAdmin(String adminId, String username, String password,
                                String fullName, String email,
                                String adminType, String createdDate) {
        Admin updated;
        if ("superadmin".equals(adminType)) {
            updated = new SuperAdmin(adminId, username, password, fullName, email, createdDate);
        } else {
            updated = new RegularAdmin(adminId, username, password, fullName, email, createdDate);
        }
        try {
            return fileHandler.updateAdmin(updated);
        } catch (Exception e) {
            throw new RuntimeException("Could not update admin: " + e.getMessage());
        }
    }

    public boolean deleteAdmin(String id) {
        try {
            return fileHandler.deleteAdmin(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete admin: " + e.getMessage());
        }
    }
}