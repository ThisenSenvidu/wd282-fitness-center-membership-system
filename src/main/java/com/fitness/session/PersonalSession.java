package com.fitness.session;

// Inheritance - PersonalSession extends Session
public class PersonalSession extends Session {

    private double fineAmount;   // fine if cancelled late
    private boolean poolSession;

    public PersonalSession() {
        super();
        setSessionType("personal");
    }

    public PersonalSession(String sessionId, String memberId, String trainerId,
                           String className, String sessionDate, String sessionTime,
                           String status, double fineAmount, boolean poolSession) {
        super(sessionId, memberId, trainerId, className, sessionDate, sessionTime, "personal", status);
        this.fineAmount  = fineAmount;
        this.poolSession = poolSession;
    }

    public double getFineAmount() {

        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public boolean isPoolSession()             { return poolSession; }
    public void setPoolSession(boolean v)      { this.poolSession = v; }

    // Polymorphism - Override getSessionSummary()
    @Override
    public String getSessionSummary() {
        return "[PERSONAL] " + getClassName()
                + " | " + getSessionDate() + " " + getSessionTime()
                + " | " + getStatus()
                + (poolSession ? " | Pool Session" : "")
                + (fineAmount > 0 ? " | Fine: Rs." + fineAmount : "");
    }

    // File handling
    @Override
    public String toFileString() {
        return super.toFileString() + ",PERSONAL," + fineAmount + "," + poolSession;
    }

    public static PersonalSession fromPersonalFileString(String line) {
        String[] p = line.split(",", 11);
        if (p.length < 11) return null;
        return new PersonalSession(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[5].trim(), p[7].trim(),
                Double.parseDouble(p[9].trim()),
                Boolean.parseBoolean(p[10].trim())
        );
    }
}