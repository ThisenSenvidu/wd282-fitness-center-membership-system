package com.fitness.fitnesstrainer.model;


public abstract class Admin {

    private String adminId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String adminType;    // "superadmin" or "admin"
    private String createdDate;

    private static int adminCount = 0;

    // No-arg constructor — Constructor Chaining
    public Admin() {
        this("", "", "", "", "", "", "");
    }

    // Full constructor
    public Admin(String adminId, String username, String password,
                 String fullName, String email,
                 String adminType, String createdDate) {
        this.adminId     = adminId;
        this.username    = username;
        this.password    = password;
        this.fullName    = fullName;
        this.email       = email;
        this.adminType   = adminType;
        this.createdDate = createdDate;
        adminCount++;
    }

    // Abstract method — Polymorphism
    // Each subclass defines its own permissions
    public abstract String getAdminPermissions();

    // Returns a display label — overridden by subclasses (Polymorphism)
    public String getAdminTypeLabel() {
        return "Admin";
    }

    // Static method — Static Modifier
    public static int getAdminCount() {
        return adminCount;
    }

    // Save admin as pipe-separated line for admins.txt
    public String toFileString() {
        return adminId + "|" + username + "|" + password + "|" +
               fullName + "|" + email + "|" + adminType + "|" + createdDate;
    }

    // Read one line from admins.txt and return correct subclass (Polymorphism)
    public static Admin fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;

        String adminId     = parts[0].trim();
        String username    = parts[1].trim();
        String password    = parts[2].trim();
        String fullName    = parts[3].trim();
        String email       = parts[4].trim();
        String adminType   = parts[5].trim();
        String createdDate = parts[6].trim();

        if (adminType.equals("superadmin")) {
            return new SuperAdmin(adminId, username, password, fullName, email, createdDate);
        } else {
            return new RegularAdmin(adminId, username, password, fullName, email, createdDate);
        }
    }

    // Getters and Setters — Encapsulation
    public String getAdminId()                     { return adminId; }
    public void   setAdminId(String adminId)       { this.adminId = adminId; }

    public String getUsername()                    { return username; }
    public void   setUsername(String username)     { this.username = username; }

    public String getPassword()                    { return password; }
    public void   setPassword(String password)     { this.password = password; }

    public String getFullName()                    { return fullName; }
    public void   setFullName(String fullName)     { this.fullName = fullName; }

    public String getEmail()                       { return email; }
    public void   setEmail(String email)           { this.email = email; }

    public String getAdminType()                   { return adminType; }
    public void   setAdminType(String adminType)   { this.adminType = adminType; }

    public String getCreatedDate()                 { return createdDate; }
    public void   setCreatedDate(String d)         { this.createdDate = d; }

    @Override
    public String toString() {
        return "Admin[id=" + adminId + ", username=" + username + ", type=" + adminType + "]";
    }
}