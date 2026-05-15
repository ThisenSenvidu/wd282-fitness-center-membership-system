package com.fitness.session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/sessions/*")
public class SessionServlet extends HttpServlet {

    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        sessionService = new SessionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "list":   listSessions(req, res);   break;
            case "add":    showAddForm(req, res);    break;
            case "edit":   showEditForm(req, res);   break;
            case "cancel": cancelSession(req, res);  break;
            case "delete": deleteSession(req, res);  break;
            case "search": searchSessions(req, res); break;
            default:       listSessions(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create": createSession(req, res); break;
            case "update": updateSession(req, res); break;
            default:       listSessions(req, res);
        }
    }

    private void listSessions(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<Session> sessions = sessionService.getAllSessions();
        req.setAttribute("sessions", sessions);
        req.getRequestDispatcher("/WEB-INF/views/session/session-list.jsp").forward(req, res);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/session/session-form.jsp").forward(req, res);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String sessionId = req.getParameter("id");
        Session session = sessionService.getSessionById(sessionId);
        if (session != null) {
            req.setAttribute("session", session);
            req.getRequestDispatcher("/WEB-INF/views/session/session-edit.jsp").forward(req, res);
        } else {
            res.sendRedirect(req.getContextPath() + "/sessions?action=list&error=notfound");
        }
    }

    private void createSession(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Session session = extractFromRequest(req);
        boolean ok = sessionService.createSession(session);
        res.sendRedirect(req.getContextPath() + "/sessions?action=list&msg=" + (ok ? "created" : "failed"));
    }

    private void updateSession(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Session session = extractFromRequest(req);
        session.setSessionId(req.getParameter("sessionId"));
        boolean ok = sessionService.updateSession(session);
        res.sendRedirect(req.getContextPath() + "/sessions?action=list&msg=" + (ok ? "updated" : "failed"));
    }

    private void cancelSession(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String sessionId = req.getParameter("id");
        sessionService.cancelSession(sessionId);
        res.sendRedirect(req.getContextPath() + "/sessions?action=list&msg=cancelled");
    }

    private void deleteSession(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String sessionId = req.getParameter("id");
        sessionService.deleteSession(sessionId);
        res.sendRedirect(req.getContextPath() + "/sessions?action=list&msg=deleted");
    }

    private void searchSessions(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Session> sessions = sessionService.searchSessions(keyword);
        req.setAttribute("sessions", sessions);
        req.setAttribute("keyword", keyword);
        req.getRequestDispatcher("/WEB-INF/views/session/session-list.jsp").forward(req, res);
    }

    private Session extractFromRequest(HttpServletRequest req) {
        String type = req.getParameter("sessionType");
        if ("personal".equals(type)) {
            PersonalSession s = new PersonalSession();
            s.setMemberId(req.getParameter("memberId"));
            s.setTrainerId(req.getParameter("trainerId"));
            s.setClassName(req.getParameter("className"));
            s.setSessionDate(req.getParameter("sessionDate"));
            s.setSessionTime(req.getParameter("sessionTime"));
            s.setStatus(req.getParameter("status") != null ? req.getParameter("status") : "pending");
            s.setFineAmount(0);
            s.setPoolSession("on".equals(req.getParameter("poolSession")));
            return s;
        } else {
            Session s = new Session();
            s.setMemberId(req.getParameter("memberId"));
            s.setTrainerId(req.getParameter("trainerId") != null ? req.getParameter("trainerId") : "");
            s.setClassName(req.getParameter("className"));
            s.setSessionDate(req.getParameter("sessionDate"));
            s.setSessionTime(req.getParameter("sessionTime"));
            s.setSessionType("group");
            s.setStatus(req.getParameter("status") != null ? req.getParameter("status") : "pending");
            return s;
        }
    }
}