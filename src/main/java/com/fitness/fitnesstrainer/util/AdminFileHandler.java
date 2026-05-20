package com.fitness.fitnesstrainer.util;

import com.fitness.fitnesstrainer.model.Admin;
import com.fitness.fitnesstrainer.model.SuperAdmin;
import com.fitness.fitnesstrainer.model.RegularAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;


@Component
public class AdminFileHandler {

    @Value("${app.admins.file-path}")
    private String filePath;

    // Runs once at startup — creates folder and file if they don't exist
    // Also seeds a default SuperAdmin account if the file is empty
    @PostConstruct
    public void initFile() {
        try {
            File file = new File(filePath);
            File dir  = file.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
                // Seed a default superadmin so someone can always log in
                seedDefaultAdmin();
            }
        } catch (IOException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    // Seeds one default SuperAdmin on first run
    private void seedDefaultAdmin() throws IOException {
        SuperAdmin defaultAdmin = new SuperAdmin(
                "ADM-1001", "admin", "admin123",
                "System Administrator", "admin@fitcenter.lk", "2024-01-01");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(defaultAdmin.toFileString());
            writer.newLine();
        }
    }

    // LOGIN CHECK — find admin by username and password
    public Admin findByUsernameAndPassword(String username, String password) throws IOException {
        for (Admin a : getAllAdmins()) {
            if (a.getUsername().equals(username.trim()) &&
                a.getPassword().equals(password.trim())) {
                return a;
            }
        }
        return null;
    }

    // CREATE — append one new admin to admins.txt
    public void addAdmin(Admin admin) throws IOException {
        String newId = generateId();
        admin.setAdminId(newId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(admin.toFileString());
            writer.newLine();
        }
    }

    // READ ALL
    public ArrayList<Admin> getAllAdmins() throws IOException {
        ArrayList<Admin> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Admin a = Admin.fromFileString(line); // Polymorphism
                    if (a != null) list.add(a);
                }
            }
        }
        return list;
    }

    // READ BY ID
    public Admin getAdminById(String adminId) throws IOException {
        for (Admin a : getAllAdmins()) {
            if (a.getAdminId().equals(adminId)) return a;
        }
        return null;
    }

    // SEARCH — filter by username or full name
    public ArrayList<Admin> searchAdmins(String keyword) throws IOException {
        ArrayList<Admin> results = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        for (Admin a : getAllAdmins()) {
            if (a.getUsername().toLowerCase().contains(kw) ||
                a.getFullName().toLowerCase().contains(kw)) {
                results.add(a);
            }
        }
        return results;
    }

    // UPDATE
    public boolean updateAdmin(Admin updated) throws IOException {
        ArrayList<Admin> all = getAllAdmins();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getAdminId().equals(updated.getAdminId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // DELETE
    public boolean deleteAdmin(String adminId) throws IOException {
        ArrayList<Admin> all = getAllAdmins();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getAdminId().equals(adminId)) {
                all.remove(i);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // Helper — overwrite entire file with updated list
    private void writeAllToFile(ArrayList<Admin> admins) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Admin a : admins) {
                writer.write(a.toFileString());
                writer.newLine();
            }
        }
    }

    // Helper — generate ID like ADM-1002, ADM-1003
    private String generateId() throws IOException {
        int nextNum = 1001 + getAllAdmins().size();
        return String.format("ADM-%d", nextNum);
    }
}