package com.fitness.fitnesstrainer.service;

import com.fitness.fitnesstrainer.model.Member;
import com.fitness.fitnesstrainer.model.PremiumMember;
import com.fitness.fitnesstrainer.model.RegularMember;
import com.fitness.fitnesstrainer.util.MemberFileHandler;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


@Service
public class MemberService {

    private final MemberFileHandler fileHandler;

    public MemberService(MemberFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ArrayList<Member> getAllMembers() {
        try {
            return fileHandler.getAllMembers();
        } catch (Exception e) {
            throw new RuntimeException("Could not load members: " + e.getMessage());
        }
    }

    public Member getMemberById(String id) {
        try {
            return fileHandler.getMemberById(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not find member: " + e.getMessage());
        }
    }

    public ArrayList<Member> searchMembers(String keyword) {
        try {
            return fileHandler.searchMembers(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }

    // Polymorphism — creates PremiumMember or RegularMember based on plan
    public void addMember(String name, String email, String phone,
                          String address, String membershipPlan, String joinDate) {
        Member member;
        if ("premium".equals(membershipPlan)) {
            member = new PremiumMember(null, name.trim(), email.trim(),
                                       phone.trim(), address.trim(), joinDate.trim());
        } else {
            member = new RegularMember(null, name.trim(), email.trim(),
                                       phone.trim(), address.trim(), joinDate.trim());
        }
        try {
            fileHandler.addMember(member);
        } catch (Exception e) {
            throw new RuntimeException("Could not save member: " + e.getMessage());
        }
    }

    public boolean updateMember(String memberId, String name, String email,
                                 String phone, String address,
                                 String membershipPlan, String joinDate) {
        Member updated;
        if ("premium".equals(membershipPlan)) {
            updated = new PremiumMember(memberId, name.trim(), email.trim(),
                                        phone.trim(), address.trim(), joinDate.trim());
        } else {
            updated = new RegularMember(memberId, name.trim(), email.trim(),
                                        phone.trim(), address.trim(), joinDate.trim());
        }
        try {
            return fileHandler.updateMember(updated);
        } catch (Exception e) {
            throw new RuntimeException("Could not update member: " + e.getMessage());
        }
    }

    public boolean deleteMember(String id) {
        try {
            return fileHandler.deleteMember(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete member: " + e.getMessage());
        }
    }
}
