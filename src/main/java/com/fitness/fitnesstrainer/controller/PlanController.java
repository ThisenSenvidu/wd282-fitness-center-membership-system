package com.fitness.fitnesstrainer.controller;

import com.fitness.fitnesstrainer.model.MembershipPlan;
import com.fitness.fitnesstrainer.service.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;


@Controller
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    // GET /plans — show all plans
    @GetMapping
    public String listPlans(Model model) {
        try {
            ArrayList<MembershipPlan> plans = planService.getAllPlans();
            model.addAttribute("plans", plans);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "plan-list";
    }

    // GET /plans/search?keyword=yoga
    @GetMapping("/search")
    public String searchPlans(@RequestParam(defaultValue = "") String keyword, Model model) {
        try {
            model.addAttribute("plans", planService.searchPlans(keyword));
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "plan-list";
    }

    // GET /plans/add — show creation form
    @GetMapping("/add")
    public String showAddForm() {
        return "plan-create";
    }

    // POST /plans/add — save new plan
    @PostMapping("/add")
    public String addPlan(@RequestParam String planName,
                          @RequestParam String price,
                          @RequestParam String durationMonths,
                          @RequestParam String description,
                          @RequestParam String planType,
                          Model model,
                          RedirectAttributes ra) {

        if (isEmpty(planName) || isEmpty(price) || isEmpty(durationMonths) ||
            isEmpty(description) || isEmpty(planType)) {
            model.addAttribute("errorMsg", "All fields are required.");
            return "plan-create";
        }

        try {
            double priceVal    = Double.parseDouble(price.trim());
            int    durationVal = Integer.parseInt(durationMonths.trim());
            planService.addPlan(planName, priceVal, durationVal, description, planType);
            ra.addFlashAttribute("successMsg", "Membership plan created successfully!");
            return "redirect:/plans";
        } catch (NumberFormatException e) {
            model.addAttribute("errorMsg", "Price must be a number and Duration must be a whole number.");
            return "plan-create";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "plan-create";
        }
    }

    // GET /plans/{id}/edit — show update form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        MembershipPlan plan = planService.getPlanById(id);
        if (plan == null) {
            model.addAttribute("errorMsg", "Plan not found.");
            return listPlans(model);
        }
        model.addAttribute("plan", plan);
        return "plan-update";
    }

    // POST /plans/{id}/edit — save updated plan
    @PostMapping("/{id}/edit")
    public String updatePlan(@PathVariable String id,
                             @RequestParam String planName,
                             @RequestParam String price,
                             @RequestParam String durationMonths,
                             @RequestParam String description,
                             @RequestParam String planType,
                             Model model,
                             RedirectAttributes ra) {

        if (isEmpty(planName) || isEmpty(price) || isEmpty(durationMonths) ||
            isEmpty(description) || isEmpty(planType)) {
            model.addAttribute("errorMsg", "All fields are required.");
            model.addAttribute("plan", planService.getPlanById(id));
            return "plan-update";
        }

        try {
            double priceVal    = Double.parseDouble(price.trim());
            int    durationVal = Integer.parseInt(durationMonths.trim());
            boolean ok = planService.updatePlan(id, planName, priceVal, durationVal, description, planType);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                                 ok ? "Plan updated successfully!" : "Plan not found.");
        } catch (NumberFormatException e) {
            model.addAttribute("errorMsg", "Price must be a number and Duration must be a whole number.");
            model.addAttribute("plan", planService.getPlanById(id));
            return "plan-update";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/plans";
    }

    // POST /plans/{id}/delete — delete plan
    @PostMapping("/{id}/delete")
    public String deletePlan(@PathVariable String id, RedirectAttributes ra) {
        try {
            boolean ok = planService.deletePlan(id);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                                 ok ? "Plan deleted." : "Plan not found.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/plans";
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
