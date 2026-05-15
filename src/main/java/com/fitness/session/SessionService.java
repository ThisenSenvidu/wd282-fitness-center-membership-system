package com.fitness.session;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SessionService {

    private static final String FILE_PATH = "data/sessions.txt";

    private void ensureFile() throws IOException {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path.getParent())) Files.createDirectories(path.getParent());
        if (!Files.exists(path)) Files.createFile(path);
    }

    private String generateId() {
        return "SES" + System.currentTimeMillis();
    }

    // ── CREATE ──────────────────────────────────────────
    public boolean createSession(Session session) {
        session.setSessionId(generateId());
        try {
            ensureFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(session.toFileString());
            writer.newLine();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── READ ALL ─────────────────────────────────────────
    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();
        try {
            ensureFile();
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                Session session = (parts.length >= 11 && "PERSONAL".equals(parts[8].trim()))
                        ? PersonalSession.fromPersonalFileString(line)
                        : Session.fromFileString(line);
                if (session != null) sessions.add(session);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessions;
    }

    // ── READ BY ID ────────────────────────────────────────
    public Session getSessionById(String sessionId) {
        for (Session s : getAllSessions()) {
            if (s.getSessionId().equals(sessionId)) return s;
        }
        return null;
    }

    // ── READ BY MEMBER ───────────────────────────────────
    public List<Session> getSessionsByMember(String memberId) {
        List<Session> result = new ArrayList<>();
        for (Session s : getAllSessions()) {
            if (s.getMemberId().equals(memberId)) result.add(s);
        }
        return result;
    }

    // ── SEARCH ───────────────────────────────────────────
    public List<Session> searchSessions(String keyword) {
        List<Session> result = new ArrayList<>();
        for (Session s : getAllSessions()) {
            if (s.getClassName().toLowerCase().contains(keyword.toLowerCase()) ||
                    s.getMemberId().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    // ── UPDATE ────────────────────────────────────────────
    public boolean updateSession(Session updated) {
        List<Session> sessions = getAllSessions();
        boolean found = false;
        List<String> lines = new ArrayList<>();
        for (Session s : sessions) {
            if (s.getSessionId().equals(updated.getSessionId())) {
                lines.add(updated.toFileString());
                found = true;
            } else {
                lines.add(s.toFileString());
            }
        }
        if (!found) return false;
        return writeAllLines(lines);
    }

    // ── CANCEL (Update status) ────────────────────────────
    public boolean cancelSession(String sessionId) {
        List<Session> sessions = getAllSessions();
        boolean found = false;
        List<String> lines = new ArrayList<>();
        for (Session s : sessions) {
            if (s.getSessionId().equals(sessionId)) {
                s.setStatus("cancelled");
                lines.add(s.toFileString());
                found = true;
            } else {
                lines.add(s.toFileString());
            }
        }
        if (!found) return false;
        return writeAllLines(lines);
    }

    // ── DELETE ────────────────────────────────────────────
    public boolean deleteSession(String sessionId) {
        List<Session> sessions = getAllSessions();
        boolean found = false;
        List<String> lines = new ArrayList<>();
        for (Session s : sessions) {
            if (s.getSessionId().equals(sessionId)) {
                found = true;
            } else {
                lines.add(s.toFileString());
            }
        }
        if (!found) return false;
        return writeAllLines(lines);
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