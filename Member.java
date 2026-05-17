package com.fitness.member;

/**
 * Member.java — Abstract base class for all gym members.
 *
 * OOP Concepts:
 *   Encapsulation      — private fields with getters/setters
 *   Abstract Class     — cannot be instantiated directly
 *   Abstraction        — abstract method getMembershipDetails()
 *   Constructor Chain  — no-arg constructor calls full constructor via this()
 *   Static Modifier    — memberCount tracks total objects created
 */



    private static int memberCount = 0;

    // No-arg constructor — Constructor Chaining
    public Member() {
        this("", "", "", "", "", "", "");
    }

    // Full constructor
    public Member(String memberId, String name, String email,
                  String phone, String address,
                  String membershipPlan, String joinDate) {
        this.memberId       = memberId;
        this.name           = name;
        this.email          = email;
        this.phone          = phone;
        this.address        = address;
        this.membershipPlan = membershipPlan;
        this.joinDate       = joinDate;
        memberCount++;
    }

    // Abstract method — polymorphism
    public abstract String getMembershipDetails();

    public String getMemberTypeLabel() {
        return "Member";
    }

    // Static method
    public static int getMemberCount() {
        return memberCount;
    }

    // Save member as pipe-separated line for members.txt
    public String toFileString() {
        return memberId + "|" + name + "|" + email + "|" +
               phone + "|" + address + "|" + membershipPlan + "|" + joinDate;
    }

    // Read one line from members.txt and return correct subclass
    public static Member fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 7) return null;
        if (parts[5].trim().equals("premium")) {
            return new PremiumMember(parts[0], parts[1], parts[2],
                                     parts[3], parts[4], parts[6]);
        } else {
            return new RegularMember(parts[0], parts[1], parts[2],
                                     parts[3], parts[4], parts[6]);
        }
    }

    // Getters and Setters — Encapsulation
    public String getMemberId()                    { return memberId; }
    public void   setMemberId(String memberId)     { this.memberId = memberId; }

    public String getName()                        { return name; }
    public void   setName(String name)             { this.name = name; }

    public String getEmail()                       { return email; }
    public void   setEmail(String email)           { this.email = email; }

    public String getPhone()                       { return phone; }
    public void   setPhone(String phone)           { this.phone = phone; }

    public String getAddress()                     { return address; }
    public void   setAddress(String address)       { this.address = address; }

    public String getMembershipPlan()                      { return membershipPlan; }
    public void   setMembershipPlan(String membershipPlan) { this.membershipPlan = membershipPlan; }

    public String getJoinDate()                    { return joinDate; }
    public void   setJoinDate(String joinDate)     { this.joinDate = joinDate; }

    @Override
    public String toString() {
        return "Member[id=" + memberId + ", name=" + name + ", plan=" + membershipPlan + "]";
    }
}

