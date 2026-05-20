package com.fitness.fitnesstrainer.controller;

import com.fitness.fitnesstrainer.model.Member;
import com.fitness.fitnesstrainer.model.MembershipPlan;
import com.fitness.fitnesstrainer.model.Trainer;
import com.fitness.fitnesstrainer.service.MemberService;
import com.fitness.fitnesstrainer.service.PlanService;
import com.fitness.fitnesstrainer.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;


@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService  memberService;
    private final PlanService    planService;     // ← NEW connection
    private final TrainerService trainerService;  // ← NEW connection

    // Spring injects all three services automatically
    public MemberController(MemberService memberService,
                            PlanService planService,
                            TrainerService trainerService) {
        this.memberService  = memberService;
        this.planService    = planService;
        this.trainerService = trainerService;
    }

    // GET /members — show all members
    @GetMapping
    public String listMembers(Model model) {
        try {
            ArrayList<Member> members = memberService.getAllMembers();
            model.addAttribute("members", members);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "member-list";
    }

    // GET /members/search?keyword=...
    @GetMapping("/search")
    public String searchMembers(@RequestParam(defaultValue = "") String keyword,
                                Model model) {
        try {
            model.addAttribute("members", memberService.searchMembers(keyword));
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "member-list";
    }

    // GET /members/add — show register form WITH plan and trainer dropdowns
    @GetMapping("/add")
    public String showAddForm(Model model) {
        loadDropdownData(model);
        return "member-register";
    }

    // POST /members/add — save new member
    @PostMapping("/add")
    public String addMember(@RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String phone,
                            @RequestParam String address,
                            @RequestParam String membershipPlan,
                            @RequestParam String joinDate,
                            Model model,
                            RedirectAttributes ra) {

        if (isEmpty(name) || isEmpty(email) || isEmpty(phone) ||
            isEmpty(address) || isEmpty(membershipPlan) || isEmpty(joinDate)) {
            model.addAttribute("errorMsg", "All fields are required.");
            loadDropdownData(model);
            return "member-register";
        }

        try {
            memberService.addMember(name, email, phone, address, membershipPlan, joinDate);
            ra.addFlashAttribute("successMsg", "Member registered successfully!");
            return "redirect:/members";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            loadDropdownData(model);
            return "member-register";
        }
    }

    // GET /members/{id} — view member profile
    @GetMapping("/{id}")
    public String viewMember(@PathVariable String id, Model model) {
        Member m = memberService.getMemberById(id);
        if (m == null) {
            model.addAttribute("errorMsg", "Member not found.");
            return listMembers(model);
        }
        model.addAttribute("member", m);
        return "member-profile";
    }

    // GET /members/{id}/edit — show update form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Member m = memberService.getMemberById(id);
        if (m == null) {
            model.addAttribute("errorMsg", "Member not found.");
            return listMembers(model);
        }
        model.addAttribute("member", m);
        loadDropdownData(model);
        return "member-update";
    }

    // POST /members/{id}/edit — save updated member
    @PostMapping("/{id}/edit")
    public String updateMember(@PathVariable String id,
                               @RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String address,
                               @RequestParam String membershipPlan,
                               @RequestParam String joinDate,
                               Model model,
                               RedirectAttributes ra) {

        if (isEmpty(name) || isEmpty(email) || isEmpty(phone) ||
            isEmpty(address) || isEmpty(membershipPlan) || isEmpty(joinDate)) {
            model.addAttribute("errorMsg", "All fields are required.");
            model.addAttribute("member", memberService.getMemberById(id));
            loadDropdownData(model);
            return "member-update";
        }

        try {
            boolean ok = memberService.updateMember(id, name, email, phone,
                                                    address, membershipPlan, joinDate);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                                 ok ? "Member updated!" : "Member not found.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/members";
    }

    // POST /members/{id}/delete — delete member
    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable String id, RedirectAttributes ra) {
        try {
            boolean ok = memberService.deleteMember(id);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                                 ok ? "Member deleted." : "Member not found.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/members";
    }

    // Helper — loads plans and trainers for dropdowns
    private void loadDropdownData(Model model) {
        try {
            ArrayList<MembershipPlan> plans = planService.getAllPlans();
            model.addAttribute("plans", plans);
        } catch (Exception e) {
            model.addAttribute("plans", new ArrayList<>());
        }
        try {
            ArrayList<Trainer> trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
        } catch (Exception e) {
            model.addAttribute("trainers", new ArrayList<>());
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
