package com.fitness.fitnesstrainer.controller;

//import classes//
import com.fitness.fitnesstrainer.model.Member;
import com.fitness.fitnesstrainer.plan.MembershipPlan;
import com.fitness.fitnesstrainer.model.Trainer;
import com.fitness.fitnesstrainer.service.MemberService;
import com.fitness.fitnesstrainer.plan.PlanService;
import com.fitness.fitnesstrainer.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;  //Imports all Spring web annotations//
import org.springframework.web.servlet.mvc.support.RedirectAttributes; //Used to pass messages after redirects//
import java.util.ArrayList;

/**
 * MemberController.java — Updated with cross-component connections.
 *
 * New connections:
 *   → Loads MembershipPlan list so the register form shows a plan dropdown
 *   → Loads Trainer list        so the register form shows a trainer dropdown
 *
 * Spring Boot concepts:
 *   Dependency Injection — Spring injects PlanService and TrainerService
 *                          alongside MemberService automatically
 */
@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService  memberService;    //Stores Memberservice object
    private final PlanService    planService;     // ← NEW connection
    private final TrainerService trainerService;  // ← NEW connection

    // Spring injects all three services automatically
    public MemberController(MemberService memberService,
                            PlanService planService,
                            TrainerService trainerService) { //Constructor for the controller
        this.memberService  = memberService;
        this.planService    = planService;
        this.trainerService = trainerService;
    }

    // GET /members — show all members
    @GetMapping
    public String listMembers(Model model) { //Create a method to display all members//
        try {
            ArrayList<Member> members = memberService.getAllMembers(); //Gets all members from service layer//
            model.addAttribute("members", members); //Send all data to frontend//
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "member-list"; //open memberlist html//
    }

    // GET /members/search?keyword=...
    @GetMapping("/search")
    public String searchMembers(@RequestParam(defaultValue = "") String keyword,
                                Model model) {
        try {
            model.addAttribute("members", memberService.searchMembers(keyword)); //Search the member using keyword//
            model.addAttribute("keyword", keyword); //send keyword back to page//
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "member-list";
    }

    // GET /members/add — show register form WITH plan and trainer dropdowns
    @GetMapping("/add")
    public String showAddForm(Model model) { //open member registration form//
        loadDropdownData(model);
        return "member-register"; //open member registration html//
    }

    // POST /members/add — save new member
    @PostMapping("/add") //Handles form submission
    public String addMember(@RequestParam String name, //connects the form field values//
                            @RequestParam String email,
                            @RequestParam String phone,
                            @RequestParam String address,
                            @RequestParam String membershipPlan,
                            @RequestParam String joinDate,
                            Model model,
                            RedirectAttributes ra) {

        if (isEmpty(name) || isEmpty(email) || isEmpty(phone) || //Check if any field is empty//
            isEmpty(address) || isEmpty(membershipPlan) || isEmpty(joinDate)) {
            model.addAttribute("errorMsg", "All fields are required.");
            loadDropdownData(model);
            return "member-register";
        }

        try {
            memberService.addMember(name, email, phone, address, membershipPlan, joinDate); //save the member in service layer//
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
        if (m == null) { //Check if the member exsist//
            model.addAttribute("errorMsg", "Member not found.");
            return listMembers(model);
        }
        model.addAttribute("member", m); //send member object to frontend//
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
            model.addAttribute("member", memberService.getMemberById(id)); //search for a member using member id//
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
            ArrayList<MembershipPlan> plans = planService.getAllPlans(); //create a list to store membership plan objects//
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
        return s == null || s.trim().isEmpty(); // check whether it is null,empty or space then return true//
    }
}
