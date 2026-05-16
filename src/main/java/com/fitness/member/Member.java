package com.fitness.member;

public class Member {

    private String memberId;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String address;
    private String email;
    private String phone;
    private String emergencyContact;
    private String memberType;   // individual / couple
    private String planId;
    private String joinDate;
    private String status;    // active / inactive
    
    public Member() {}

    public Member(String memberId, String firstName, String lastName, String dob,
                  String gender, String address, String email, String phone,
                  String emergencyContact, String memberType, String planId,
                  String joinDate, String status) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.memberType = memberType;
        this.planId = planId;
        this.joinDate = joinDate;
        this.status = status;
    }

    // Encapsulation - Getters & Setters
    public String getMemberId()                        { return memberId; }
    public void setMemberId(String memberId)           { this.memberId = memberId; }

    public String getFirstName()                       { return firstName; }
    public void setFirstName(String firstName)         { this.firstName = firstName; }

    public String getLastName()                        { return lastName; }
    public void setLastName(String lastName)           { this.lastName = lastName; }

    public String getFullName()                        { return firstName + " " + lastName; }

    public String getDob()                             { return dob; }
    public void setDob(String dob)                     { this.dob = dob; }

    public String getGender()                          { return gender; }
    public void setGender(String gender)               { this.gender = gender; }

    public String getAddress()                         { return address; }
    public void setAddress(String address)             { this.address = address; }

    public String getEmail()                           { return email; }
    public void setEmail(String email)                 { this.email = email; }

    public String getPhone()                           { return phone; }
    public void setPhone(String phone)                 { this.phone = phone; }

    public String getEmergencyContact()                        { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact)   { this.emergencyContact = emergencyContact; }

    public String getMemberType()                      { return memberType; }
    public void setMemberType(String memberType)       { this.memberType = memberType; }

    public String getPlanId()                          { return planId; }
    public void setPlanId(String planId)               { this.planId = planId; }

    public String getJoinDate()                        { return joinDate; }
    public void setJoinDate(String joinDate)           { this.joinDate = joinDate; }

    public String getStatus()                          { return status; }
    public void setStatus(String status)               { this.status = status; }

    // Polymorphism - subclasses override
    public String getMemberSummary() {
        return getFullName() + " | " + memberType + " | " + planId + " | " + status;
    }

    // File handling - serialize to CSV line
    public String toFileString() {
        return memberId + "," + firstName + "," + lastName + "," + dob + "," +
                gender + "," + address.replace(",", ";") + "," + email + "," +
                phone + "," + emergencyContact + "," + memberType + "," +
                planId + "," + joinDate + "," + status;
    }

    // File handling - deserialize from CSV line
    public static Member fromFileString(String line) {
        String[] p = line.split(",", 13);
        if (p.length < 13) return null;
        return new Member(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[5].replace(";", ",").trim(), p[6].trim(),
                p[7].trim(), p[8].trim(), p[9].trim(), p[10].trim(),
                p[11].trim(), p[12].trim()
        );
    }
}
