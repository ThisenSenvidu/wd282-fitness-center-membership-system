package com.fitness.member;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MemberService {

    private static final String FILE_PATH = "data/members.txt";

    private void ensureFile() throws IOException {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path.getParent())) Files.createDirectories(path.getParent());
        if (!Files.exists(path)) Files.createFile(path);
    }

    private String generateId() {
        return "MEM" + System.currentTimeMillis();
    }

    // ── CREATE ──────────────────────────────────────────
    public boolean createMember(Member member) {
        member.setMemberId(generateId());
        try {
            ensureFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(member.toFileString());
            writer.newLine();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── READ ALL ─────────────────────────────────────────
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try {
            ensureFile();
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                Member member = (parts.length >= 14 && "PREMIUM".equals(parts[13].trim()))
                        ? PremiumMember.fromPremiumFileString(line)
                        : Member.fromFileString(line);
                if (member != null) members.add(member);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return members;
    }

    // ── READ BY ID ────────────────────────────────────────
    public Member getMemberById(String memberId) {
        for (Member m : getAllMembers()) {
            if (m.getMemberId().equals(memberId)) return m;
        }
        return null;
    }

    // ── SEARCH ───────────────────────────────────────────
    public List<Member> searchMembers(String keyword) {
        List<Member> result = new ArrayList<>();
        for (Member m : getAllMembers()) {
            if (m.getFullName().toLowerCase().contains(keyword.toLowerCase())
                    || m.getEmail().toLowerCase().contains(keyword.toLowerCase())
                    || m.getMemberId().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }

    // ── FILTER BY TYPE ────────────────────────────────────
    public List<Member> getMembersByType(String type) {
        List<Member> result = new ArrayList<>();
        for (Member m : getAllMembers()) {
            if (m.getMemberType().equalsIgnoreCase(type)) result.add(m);
        }
        return result;
    }

    // ── UPDATE ────────────────────────────────────────────
    public boolean updateMember(Member updated) {
        List<Member> members = getAllMembers();
        boolean found = false;
        List<String> lines = new ArrayList<>();
        for (Member m : members) {
            if (m.getMemberId().equals(updated.getMemberId())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(m.toFileString());
            }
        }
        if (!found) return false;
        return writeAllLines(lines);
    }

    // ── DELETE ────────────────────────────────────────────
    public boolean deleteMember(String memberId) {
        List<Member> members = getAllMembers();
        boolean found = false;
        List<String> lines = new ArrayList<>();
        for (Member m : members) {
            if (m.getMemberId().equals(memberId)) {
                found = true;
            } else {
                lines.add(m.toFileString());
            }
        }
        if (!found) return false;
        return writeAllLines(lines);
    }

    // ── STATS ──────────────────────────────────────────
    public int countActive() {
        int c = 0;
        for (Member m : getAllMembers()) if ("active".equals(m.getStatus())) c++;
        return c;
    }

    public int countPremium() {
        int c = 0;
        for (Member m : getAllMembers()) if (m instanceof PremiumMember) c++;
        return c;
    }

    private boolean writeAllLines(List<String> lines) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false));
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}