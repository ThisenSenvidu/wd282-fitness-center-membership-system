package com.fitness.fitnesstrainer.controller;

import com.fitness.fitnesstrainer.model.Trainer;
import com.fitness.fitnesstrainer.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // show all trainers
    @GetMapping
    public String listTrainers(Model model) {
        try {
            ArrayList<Trainer> trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "trainer-list";
    }

    // search?keyword=yoga
    @GetMapping("/search")
    public String searchTrainers(@RequestParam(defaultValue="") String keyword, Model model) {
        try {
            model.addAttribute("trainers", trainerService.searchTrainers(keyword));
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "trainer-list";
    }

    // show registration form
    @GetMapping("/add")
    public String showAddForm() { return "trainer-register"; }

    // save new trainer
    @PostMapping("/add")
    public String addTrainer(@RequestParam String name, @RequestParam String email, @RequestParam String phone, @RequestParam String specialization,
                             @RequestParam String availability, @RequestParam String trainerType, Model model, RedirectAttributes ra) {

        // 1. Check required fields
        if (isEmpty(name) || isEmpty(email) || isEmpty(phone) || isEmpty(specialization) || isEmpty(availability) || isEmpty(trainerType)) {

            model.addAttribute("errorMsg", "All fields are required.");
            return "trainer-register";
        }

        // 2. Email validation
        if (!email.contains("@")) {
            model.addAttribute("errorMsg", "Invalid email format.");
            return "trainer-register";
        }

        // 3. Phone validation
        if (phone.length() != 10) {
            model.addAttribute("errorMsg", "Phone must be 10 digits.");
            return "trainer-register";
        }

        try {
            trainerService.addTrainer(name,email,phone,specialization,availability,trainerType);
            ra.addFlashAttribute("successMsg","Trainer registered successfully!");
            return "redirect:/trainers";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "trainer-register";
        }
    }

    // view profile
    @GetMapping("/{id}")
    public String viewTrainer(@PathVariable String id, Model model) {
        Trainer t = trainerService.getTrainerById(id);
        if (t == null) { model.addAttribute("errorMsg","Trainer not found."); return listTrainers(model); }
        model.addAttribute("trainer", t);
        return "trainer-profile";
    }

    // show update form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Trainer t = trainerService.getTrainerById(id);
        if (t == null) { model.addAttribute("errorMsg","Trainer not found."); return listTrainers(model); }
        model.addAttribute("trainer", t);
        return "trainer-update";
    }

    // save updated trainer
    @PostMapping("/{id}/edit")
    public String updateTrainer(@PathVariable String id, @RequestParam String name, @RequestParam String email, @RequestParam String phone,
                                @RequestParam String specialization, @RequestParam String availability, @RequestParam String trainerType,
                                Model model, RedirectAttributes ra) {

        if (isEmpty(name)||isEmpty(email)||isEmpty(phone)|| isEmpty(specialization)||isEmpty(availability)||isEmpty(trainerType)) {
            model.addAttribute("errorMsg","All fields are required.");
            model.addAttribute("trainer", trainerService.getTrainerById(id));
            return "trainer-update";
        }
        try {
            boolean ok = trainerService.updateTrainer(id,name,email,phone,specialization,availability,trainerType);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                    ok ? "Trainer updated!" : "Trainer not found.");
        } catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/trainers";
    }

    // delete trainer
    @PostMapping("/{id}/delete")
    public String deleteTrainer(@PathVariable String id, RedirectAttributes ra) {
        try {
            boolean ok = trainerService.deleteTrainer(id);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                    ok ? "Trainer deleted." : "Trainer not found.");
        } catch (Exception e) { ra.addFlashAttribute("errorMsg", e.getMessage()); }
        return "redirect:/trainers";
    }

    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
}
