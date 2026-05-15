package com.fitness.session;

public class Session {

    private String sessionId;
    private String memberId;
    private String trainerId;
    private String className;
    private String sessionDate;
    private String sessionTime;
    private String sessionType;   // personal / group
    private String status;        // confirmed / pending / cancelled / completed

    public Session() {}

    public Session(String sessionId, String memberId, String trainerId,
                   String className, String sessionDate, String sessionTime,
                   String sessionType, String status) {
        this.sessionId   = sessionId;
        this.memberId    = memberId;
        this.trainerId   = trainerId;
        this.className   = className;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionType = sessionType;
        this.status      = status;
    }

    // Getters & Setters (Encapsulation)
    public String getSessionId()                   { return sessionId; }
    public void setSessionId(String sessionId)     { this.sessionId = sessionId; }

    public String getMemberId()                    { return memberId; }
    public void setMemberId(String memberId)       { this.memberId = memberId; }

    public String getTrainerId()                   { return trainerId; }
    public void setTrainerId(String trainerId)     { this.trainerId = trainerId; }

    public String getClassName()                   { return className; }
    public void setClassName(String className)     { this.className = className; }

    public String getSessionDate()                     { return sessionDate; }
    public void setSessionDate(String sessionDate)     { this.sessionDate = sessionDate; }

    public String getSessionTime()                     { return sessionTime; }
    public void setSessionTime(String sessionTime)     { this.sessionTime = sessionTime; }

    public String getSessionType()                     { return sessionType; }
    public void setSessionType(String sessionType)     { this.sessionType = sessionType; }

    public String getStatus()                  { return status; }
    public void setStatus(String status)       { this.status = status; }

    // Polymorphism - subclasses override
    public String getSessionSummary() {
        return className + " | " + sessionType + " | " + sessionDate + " " + sessionTime + " | " + status;
    }

    // File handling - serialize
    public String toFileString() {
        return sessionId + "," + memberId + "," + trainerId + "," + className + ","
                + sessionDate + "," + sessionTime + "," + sessionType + "," + status;
    }

    // File handling - deserialize
    public static Session fromFileString(String line) {
        String[] p = line.split(",", 8);
        if (p.length < 8) return null;
        return new Session(
                p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim()
        );
    }
}