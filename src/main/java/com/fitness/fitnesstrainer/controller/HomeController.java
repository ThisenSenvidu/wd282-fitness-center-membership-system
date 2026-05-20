package com.fitness.fitnesstrainer.controller;

import com.fitness.fitnesstrainer.service.MemberService;
import com.fitness.fitnesstrainer.service.TrainerService;
import com.fitness.fitnesstrainer.service.PlanService;
import com.fitness.fitnesstrainer.service.PaymentService;
import com.fitness.fitnesstrainer.service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    private final TrainerService trainerService;
    private final MemberService  memberService;
    private final PlanService    planService;
    private final PaymentService paymentService;
    private final WorkoutService workoutService;

    public HomeController(TrainerService trainerService,
                          MemberService  memberService,
                          PlanService    planService,
                          PaymentService paymentService,
                          WorkoutService workoutService) {
        this.trainerService = trainerService;
        this.memberService  = memberService;
        this.planService    = planService;
        this.paymentService = paymentService;
        this.workoutService = workoutService;
    }

    // GET / and GET /home  →  homepage
    @GetMapping({"/", "/home"})
    public String home(Model model) {

        // Load live counts — if a component isn't merged yet, defaults to 0
        try { model.addAttribute("totalTrainers", trainerService.getAllTrainers().size()); }
        catch (Exception e) { model.addAttribute("totalTrainers", 0); }

        try { model.addAttribute("totalMembers",  memberService.getAllMembers().size()); }
        catch (Exception e) { model.addAttribute("totalMembers",  0); }

        try { model.addAttribute("totalPlans",    planService.getAllPlans().size()); }
        catch (Exception e) { model.addAttribute("totalPlans",    0); }

        try { model.addAttribute("totalPayments", paymentService.getAllPayments().size()); }
        catch (Exception e) { model.addAttribute("totalPayments", 0); }

        try { model.addAttribute("totalWorkouts", workoutService.getAllPrograms().size()); }
        catch (Exception e) { model.addAttribute("totalWorkouts", 0); }

        return "home";   // → src/main/resources/templates/home.html
    }
}