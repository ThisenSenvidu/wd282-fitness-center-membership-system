package com.fitness.fitnesstrainer.util;

import com.fitness.fitnesstrainer.model.Member;
import com.fitness.fitnesstrainer.model.PremiumMember;
import com.fitness.fitnesstrainer.model.RegularMember;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;

/**
 * MemberFileHandler.java — Handles all file I/O for members.txt
 *
 * OOP / Lecture concepts:
 *   Exception Handling — try-catch on all file operations
 *   Collections        — ArrayList<Member>
 *   Generics           — ArrayList<Member>
 *   Static Modifier    — all via @Component (Spring singleton)
 *   String manipulation — split(), trim(), toLowerCase(), contains()
 */
@Component
public class MemberFileHandler {

    @Value("${app.members.file-path}")
    private String filePath;

    @PostConstruct
    public void initFile() {
        try {
            File file = new File(filePath);
            File dir  = file.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            if (!file.exists())               file.createNewFile();
        } catch (IOException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    // CREATE
    public void addMember(Member member) throws IOException {
        String newId = generateId();
        member.setMemberId(newId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(member.toFileString());
            writer.newLine();
        }
    }

    // READ ALL
    public ArrayList<Member> getAllMembers() throws IOException {
        ArrayList<Member> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Member m = Member.fromFileString(line);
                    if (m != null) list.add(m);
                }
            }
        }
        return list;
    }

    // READ BY ID
    public Member getMemberById(String memberId) throws IOException {
        for (Member m : getAllMembers()) {
            if (m.getMemberId().equals(memberId)) return m;
        }
        return null;
    }

    // SEARCH by name or email
    public ArrayList<Member> searchMembers(String keyword) throws IOException {
        ArrayList<Member> results = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        for (Member m : getAllMembers()) {
            if (m.getName().toLowerCase().contains(kw) ||
                m.getEmail().toLowerCase().contains(kw)) {
                results.add(m);
            }
        }
        return results;
    }

    // UPDATE
    public boolean updateMember(Member updated) throws IOException {
        ArrayList<Member> all = getAllMembers();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getMemberId().equals(updated.getMemberId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // DELETE
    public boolean deleteMember(String memberId) throws IOException {
        ArrayList<Member> all = getAllMembers();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getMemberId().equals(memberId)) {
                all.remove(i);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    private void writeAllToFile(ArrayList<Member> members) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Member m : members) {
                writer.write(m.toFileString());
                writer.newLine();
            }
        }
    }

    private String generateId() throws IOException {
        int nextNum = 2001 + getAllMembers().size();
        return String.format("MBR-%d", nextNum);
    }
}
