package com.fitness.member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/members/*")
public class MemberServlet extends HttpServlet {

    private MemberService memberService;

    @Override
    public void init() throws ServletException {
        memberService = new MemberService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "list":   listMembers(req, res);   break;
            case "add":    showAddForm(req, res);   break;
            case "edit":   showEditForm(req, res);  break;
            case "delete": deleteMember(req, res);  break;
            case "search": searchMembers(req, res); break;
            default:       listMembers(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create": createMember(req, res); break;
            case "update": updateMember(req, res); break;
            default:       listMembers(req, res);
        }
    }

    private void listMembers(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<Member> members = memberService.getAllMembers();
        req.setAttribute("members", members);
        req.setAttribute("totalCount", members.size());
        req.setAttribute("activeCount", memberService.countActive());
        req.setAttribute("premiumCount", memberService.countPremium());
        req.getRequestDispatcher("/WEB-INF/views/member/member-list.jsp").forward(req, res);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/member/member-form.jsp").forward(req, res);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String memberId = req.getParameter("id");
        Member member = memberService.getMemberById(memberId);
        if (member != null) {
            req.setAttribute("member", member);
            req.getRequestDispatcher("/WEB-INF/views/member/member-edit.jsp").forward(req, res);
        } else {
            res.sendRedirect(req.getContextPath() + "/members?action=list&error=notfound");
        }
    }

    private void createMember(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Member member = extractFromRequest(req);
        boolean ok = memberService.createMember(member);
        res.sendRedirect(req.getContextPath() + "/members?action=list&msg=" + (ok ? "created" : "failed"));
    }

    private void updateMember(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Member member = extractFromRequest(req);
        member.setMemberId(req.getParameter("memberId"));
        boolean ok = memberService.updateMember(member);
        res.sendRedirect(req.getContextPath() + "/members?action=list&msg=" + (ok ? "updated" : "failed"));
    }

    private void deleteMember(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String memberId = req.getParameter("id");
        memberService.deleteMember(memberId);
        res.sendRedirect(req.getContextPath() + "/members?action=list&msg=deleted");
    }

    private void searchMembers(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Member> members = memberService.searchMembers(keyword);
        req.setAttribute("members", members);
        req.setAttribute("keyword", keyword);
        req.getRequestDispatcher("/WEB-INF/views/member/member-list.jsp").forward(req, res);
    }

    private Member extractFromRequest(HttpServletRequest req) {
        boolean isPremium = "true".equals(req.getParameter("isPremium"));
        if (isPremium) {
            PremiumMember m = new PremiumMember();
            fillBasicFields(m, req);
            m.setPoolAccess(true);
            m.setSpaAccess(true);
            m.setNutritionPlan("Standard");
            m.setPersonalTrainerSessions(4);
            return m;
        } else {
            Member m = new Member();
            fillBasicFields(m, req);
            return m;
        }
    }

    private void fillBasicFields(Member m, HttpServletRequest req) {
        m.setFirstName(req.getParameter("firstName"));
        m.setLastName(req.getParameter("lastName"));
        m.setDob(req.getParameter("dob"));
        m.setGender(req.getParameter("gender"));
        m.setAddress(req.getParameter("address") != null ? req.getParameter("address") : "");
        m.setEmail(req.getParameter("email"));
        m.setPhone(req.getParameter("phone"));
        m.setEmergencyContact(req.getParameter("emergencyContact") != null ? req.getParameter("emergencyContact") : "");
        m.setMemberType(req.getParameter("memberType") != null ? req.getParameter("memberType") : "individual");
        m.setPlanId(req.getParameter("planId") != null ? req.getParameter("planId") : "");
        m.setJoinDate(req.getParameter("joinDate"));
        m.setStatus(req.getParameter("status") != null ? req.getParameter("status") : "active");
    }
}