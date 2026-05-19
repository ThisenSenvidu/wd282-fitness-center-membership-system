package com.fitness.session;

import com.fitness.session.WorkoutProgram;
import com.fitness.session.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;


@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // GET /workouts — show all programs
    @GetMapping
    public String listPrograms(Model model) {
        try {
            ArrayList<WorkoutProgram> programs = workoutService.getAllPrograms();
            model.addAttribute("programs", programs);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "workout-list";
    }

    // GET /workouts/search?keyword=cardio
    @GetMapping("/search")
    public String searchPrograms(@RequestParam(defaultValue = "") String keyword, Model model) {
        try {
            model.addAttribute("programs", workoutService.searchPrograms(keyword));
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "workout-list";
    }

    // GET /workouts/add — show creation form
    @GetMapping("/add")
    public String showAddForm() {
        return "workout-create";
    }

    // POST /workouts/add — save new program
    @PostMapping("/add")
    public String addProgram(@RequestParam String programName,
                             @RequestParam String targetGoal,
                             @RequestParam String durationWeeks,
                             @RequestParam String exercises,
                             @RequestParam String difficultyLevel,
                             @RequestParam String programType,
                             Model model,
                             RedirectAttributes ra) {

        if (isEmpty(programName) || isEmpty(targetGoal) || isEmpty(durationWeeks) ||
                isEmpty(exercises)   || isEmpty(difficultyLevel) || isEmpty(programType)) {
            model.addAttribute("errorMsg", "All fields are required.");
            return "workout-create";
        }

        try {
            int weeks = Integer.parseInt(durationWeeks.trim());
            workoutService.addProgram(programName, targetGoal, weeks,
                    exercises, difficultyLevel, programType);
            ra.addFlashAttribute("successMsg", "Workout program created successfully!");
            return "redirect:/workouts";
        } catch (NumberFormatException e) {
            model.addAttribute("errorMsg", "Duration must be a whole number (e.g. 4, 8, 12).");
            return "workout-create";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "workout-create";
        }
    }

    // GET /workouts/{id} — view program profile
    @GetMapping("/{id}")
    public String viewProgram(@PathVariable String id, Model model) {
        WorkoutProgram program = workoutService.getProgramById(id);
        if (program == null) {
            model.addAttribute("errorMsg", "Program not found.");
            return listPrograms(model);
        }
        model.addAttribute("program", program);
        return "workout-profile";
    }

    // GET /workouts/{id}/edit — show update form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        WorkoutProgram program = workoutService.getProgramById(id);
        if (program == null) {
            model.addAttribute("errorMsg", "Program not found.");
            return listPrograms(model);
        }
        model.addAttribute("program", program);
        return "workout-update";
    }

    // POST /workouts/{id}/edit — save updated program
    @PostMapping("/{id}/edit")
    public String updateProgram(@PathVariable String id,
                                @RequestParam String programName,
                                @RequestParam String targetGoal,
                                @RequestParam String durationWeeks,
                                @RequestParam String exercises,
                                @RequestParam String difficultyLevel,
                                @RequestParam String programType,
                                Model model,
                                RedirectAttributes ra) {

        if (isEmpty(programName) || isEmpty(targetGoal) || isEmpty(durationWeeks) ||
                isEmpty(exercises)   || isEmpty(difficultyLevel) || isEmpty(programType)) {
            model.addAttribute("errorMsg", "All fields are required.");
            model.addAttribute("program", workoutService.getProgramById(id));
            return "workout-update";
        }

        try {
            int weeks = Integer.parseInt(durationWeeks.trim());
            boolean ok = workoutService.updateProgram(id, programName, targetGoal,
                    weeks, exercises,
                    difficultyLevel, programType);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                    ok ? "Program updated successfully!" : "Program not found.");
        } catch (NumberFormatException e) {
            model.addAttribute("errorMsg", "Duration must be a whole number.");
            model.addAttribute("program", workoutService.getProgramById(id));
            return "workout-update";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/workouts";
    }

    // POST /workouts/{id}/delete — delete program
    @PostMapping("/{id}/delete")
    public String deleteProgram(@PathVariable String id, RedirectAttributes ra) {
        try {
            boolean ok = workoutService.deleteProgram(id);
            ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                    ok ? "Program deleted successfully." : "Program not found.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/workouts";
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}