package com.fitness.fitnesstrainer.controller;

import com.fitness.fitnesstrainer.model.Admin;
import com.fitness.fitnesstrainer.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── Helper: check if admin is logged in ───────────────────────────────────
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInAdmin") != null;
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────

    // GET /admin/login — show login page
    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // If already logged in, go straight to dashboard
        if (isLoggedIn(session)) return "redirect:/admin/dashboard";
        return "admin-login";
    }

    // POST /admin/login — check credentials
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        if (isEmpty(username) || isEmpty(password)) {
            model.addAttribute("errorMsg", "Username and password are required.");
            return "admin-login";
        }

        try {
            Admin admin = adminService.login(username, password);
            if (admin != null) {
                // Store the logged-in admin in the session
                session.setAttribute("loggedInAdmin", admin);
                return "redirect:/admin/dashboard";
            } else {
                model.addAttribute("errorMsg", "Invalid username or password.");
                return "admin-login";
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Login error: " + e.getMessage());
            return "admin-login";
        }
    }

    // GET /admin/logout — clear session and go back to login
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate();
        ra.addFlashAttribute("successMsg", "Logged out successfully.");
        return "redirect:/admin/login";
    }

    // ── DASHBOARD ─────────────────────────────────────────────────────────────

    // GET /admin/dashboard — main dashboard page
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";

        Admin loggedIn = (Admin) session.getAttribute("loggedInAdmin");
        model.addAttribute("loggedInAdmin", loggedIn);

        // Pass summary counts to dashboard
        try {
            model.addAttribute("adminCount",   adminService.getAllAdmins().size());
        } catch (Exception e) {
            model.addAttribute("adminCount", 0);
        }

        return "admin-dashboard";
    }

    // ── MANAGE ADMINS ─────────────────────────────────────────────────────────

    // GET /admin/manage — list all admin accounts
    @GetMapping("/manage")
    public String manageAdmins(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";

        Admin loggedIn = (Admin) session.getAttribute("loggedInAdmin");
        model.addAttribute("loggedInAdmin", loggedIn);

        try {
            ArrayList<Admin> admins = adminService.getAllAdmins();
            model.addAttribute("admins", admins);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "admin-manage";
    }

    // GET /admin/manage/search
    @GetMapping("/manage/search")
    public String searchAdmins(@RequestParam(defaultValue = "") String keyword,
                                HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";

        Admin loggedIn = (Admin) session.getAttribute("loggedInAdmin");
        model.addAttribute("loggedInAdmin", loggedIn);

        try {
            model.addAttribute("admins",  adminService.searchAdmins(keyword));
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "admin-manage";
    }

    // GET /admin/manage/add — show add admin form
    @GetMapping("/manage/add")
    public String showAddForm(HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        return "admin-add";
    }

    // POST /admin/manage/add — save new admin
    @PostMapping("/manage/add")
    public String addAdmin(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String adminType,
                           @RequestParam String createdDate,
                           HttpSession session,
                           Model model,
                           RedirectAttributes ra) {

        if (!isLoggedIn(session)) return "redirect:/admin/login";

        if (isEmpty(username) || isEmpty(password) || isEmpty(fullName) ||
            isEmpty(email)    || isEmpty(adminType) || isEmpty(createdDate)) {
            model.addAttribute("errorMsg", "All fields are required.");
            return "admin-add";
        }

        try {
            adminService.addAdmin(username, password, fullName, email, adminType, createdDate);
            ra.addFlashAttribute("successMsg", "Admin account created successfully!");
            return "redirect:/admin/manage";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "admin-add";
        }
    }

    // GET /admin/manage/{id}/edit — show edit form
    @GetMapping("/manage/{id}/edit")
    public String showEditForm(@PathVariable String id,
                               HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";

        Admin admin = adminService.getAdminById(id);
        if (admin == null) {
            model.addAttribute("errorMsg", "Admin not found.");
            return manageAdmins(session, model);
        }
        Admin loggedIn = (Admin) session.getAttribute("loggedInAdmin");
        model.addAttribute("loggedInAdmin", loggedIn);
        model.addAttribute("admin", admin);
        return "admin-edit";
    }

    // POST /admin/manage/{id}/edit — save updated admin
    @PostMapping("/manage/{id}/edit")
    public String updateAdmin(@PathVariable String id,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String fullName,
                              @RequestParam String email,
                              @RequestParam String adminType,
                              @RequestParam String createdDate,
                              HttpSession session,
                              Model model,
                              RedirectAttributes ra) {

        if (!isLoggedIn(session)) return "redirect:/admin/login";

        if (isEmpty(username) || isEmpty(password) || isEmpty(fullName) ||
            isEmpty(email)    || isEmpty(adminType) || isEmpty(createdDate)) {
            model.addAttribute("errorMsg", "All fields are required.");
            model.addAttribute("admin", adminService.getAdminById(id));
            return "admin-edit";
        }

        try {
            boolean ok = adminService.updateAdmin(id, username, password,
                                                  fullName, email, adminType, createdDate);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                                 ok ? "Admin updated successfully!" : "Admin not found.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/manage";
    }

    // POST /admin/manage/{id}/delete — delete admin
    @PostMapping("/manage/{id}/delete")
    public String deleteAdmin(@PathVariable String id,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";

        try {
            boolean ok = adminService.deleteAdmin(id);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                                 ok ? "Admin deleted." : "Admin not found.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/manage";
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}