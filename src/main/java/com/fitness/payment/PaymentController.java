package com.fitness.payment;

public class PaymentController {package com.fitness.fitnesstrainer.controller;

import com.fitness.fitnesstrainer.model.Member;
import com.fitness.fitnesstrainer.plan.MembershipPlan;
import com.fitness.fitnesstrainer.model.Payment;
import com.fitness.fitnesstrainer.service.MemberService;
import com.fitness.fitnesstrainer.service.PaymentService;
import com.fitness.fitnesstrainer.plan.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;

    /**
     * PaymentController.java — Updated with cross-component connections.
     *
     * New connections:
     *   → Loads Member list     so the payment form shows a member dropdown
     *   → Loads MembershipPlan  so the payment form can auto-fill the amount
     *
     * Spring Boot concepts:
     *   Dependency Injection — Spring injects MemberService and PlanService
     *                          alongside PaymentService automatically
     */
    @Controller
    @RequestMapping("/payments")
    public class PaymentController {

        private final PaymentService  paymentService;
        private final MemberService   memberService;   // ← NEW connection
        private final PlanService     planService;     // ← NEW connection

        // Spring injects all three services automatically
        public PaymentController(PaymentService paymentService,
                                 MemberService memberService,
                                 PlanService planService) {
            this.paymentService = paymentService;
            this.memberService  = memberService;
            this.planService    = planService;
        }

        // GET /payments — show all payments
        @GetMapping
        public String listPayments(Model model) {
            try {
                ArrayList<Payment> payments = paymentService.getAllPayments();
                model.addAttribute("payments", payments);
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
            }
            return "payment-list";
        }

        // GET /payments/search?keyword=john
        @GetMapping("/search")
        public String searchPayments(@RequestParam(defaultValue = "") String keyword,
                                     Model model) {
            try {
                model.addAttribute("payments", paymentService.searchPayments(keyword));
                model.addAttribute("keyword", keyword);
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
            }
            return "payment-list";
        }

        // GET /payments/add — show payment form WITH member and plan dropdowns
        @GetMapping("/add")
        public String showAddForm(Model model) {
            try {
                // Load members from members.txt → for the member dropdown
                ArrayList<Member> members = memberService.getAllMembers();
                model.addAttribute("members", members);
            } catch (Exception e) {
                // If member component not yet merged, show empty dropdown
                model.addAttribute("members", new ArrayList<>());
            }
            try {
                // Load plans from plans.txt → for the plan/amount dropdown
                ArrayList<MembershipPlan> plans = planService.getAllPlans();
                model.addAttribute("plans", plans);
            } catch (Exception e) {
                model.addAttribute("plans", new ArrayList<>());
            }
            return "payment-add";
        }

        // POST /payments/add — save new payment
        @PostMapping("/add")
        public String addPayment(@RequestParam String memberId,
                                 @RequestParam String memberName,
                                 @RequestParam String amount,
                                 @RequestParam String paymentDate,
                                 @RequestParam String paymentStatus,
                                 @RequestParam String paymentType,
                                 Model model,
                                 RedirectAttributes ra) {

            if (isEmpty(memberId) || isEmpty(memberName) || isEmpty(amount) ||
                    isEmpty(paymentDate) || isEmpty(paymentStatus) || isEmpty(paymentType)) {
                model.addAttribute("errorMsg", "All fields are required.");
                loadDropdownData(model);
                return "payment-add";
            }

            try {
                double amountVal = Double.parseDouble(amount.trim());
                paymentService.addPayment(memberId, memberName, amountVal,
                        paymentDate, paymentStatus, paymentType);
                ra.addFlashAttribute("successMsg", "Payment recorded successfully!");
                return "redirect:/payments";
            } catch (NumberFormatException e) {
                model.addAttribute("errorMsg", "Amount must be a valid number.");
                loadDropdownData(model);
                return "payment-add";
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
                loadDropdownData(model);
                return "payment-add";
            }
        }

        // GET /payments/{id} — view payment profile
        @GetMapping("/{id}")
        public String viewPayment(@PathVariable String id, Model model) {
            Payment payment = paymentService.getPaymentById(id);
            if (payment == null) {
                model.addAttribute("errorMsg", "Payment not found.");
                return listPayments(model);
            }
            model.addAttribute("payment", payment);
            return "payment-profile";
        }

        // GET /payments/{id}/edit — show update form
        @GetMapping("/{id}/edit")
        public String showEditForm(@PathVariable String id, Model model) {
            Payment payment = paymentService.getPaymentById(id);
            if (payment == null) {
                model.addAttribute("errorMsg", "Payment not found.");
                return listPayments(model);
            }
            model.addAttribute("payment", payment);
            loadDropdownData(model);
            return "payment-update";
        }

        // POST /payments/{id}/edit — save updated payment
        @PostMapping("/{id}/edit")
        public String updatePayment(@PathVariable String id,
                                    @RequestParam String memberId,
                                    @RequestParam String memberName,
                                    @RequestParam String amount,
                                    @RequestParam String paymentDate,
                                    @RequestParam String paymentStatus,
                                    @RequestParam String paymentType,
                                    Model model,
                                    RedirectAttributes ra) {

            if (isEmpty(memberId) || isEmpty(memberName) || isEmpty(amount) ||
                    isEmpty(paymentDate) || isEmpty(paymentStatus) || isEmpty(paymentType)) {
                model.addAttribute("errorMsg", "All fields are required.");
                model.addAttribute("payment", paymentService.getPaymentById(id));
                loadDropdownData(model);
                return "payment-update";
            }

            try {
                double amountVal = Double.parseDouble(amount.trim());
                boolean ok = paymentService.updatePayment(id, memberId, memberName,
                        amountVal, paymentDate,
                        paymentStatus, paymentType);
                ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                        ok ? "Payment updated!" : "Payment not found.");
            } catch (NumberFormatException e) {
                model.addAttribute("errorMsg", "Amount must be a valid number.");
                model.addAttribute("payment", paymentService.getPaymentById(id));
                loadDropdownData(model);
                return "payment-update";
            } catch (Exception e) {
                ra.addFlashAttribute("errorMsg", e.getMessage());
            }
            return "redirect:/payments";
        }

        // POST /payments/{id}/delete — delete payment
        @PostMapping("/{id}/delete")
        public String deletePayment(@PathVariable String id, RedirectAttributes ra) {
            try {
                boolean ok = paymentService.deletePayment(id);
                ra.addFlashAttribute(ok ? "successMsg" : "errorMsg",
                        ok ? "Payment deleted." : "Payment not found.");
            } catch (Exception e) {
                ra.addFlashAttribute("errorMsg", e.getMessage());
            }
            return "redirect:/payments";
        }

        // Helper — loads member and plan lists for dropdowns
        private void loadDropdownData(Model model) {
            try {
                model.addAttribute("members", memberService.getAllMembers());
            } catch (Exception e) {
                model.addAttribute("members", new ArrayList<>());
            }
            try {
                model.addAttribute("plans", planService.getAllPlans());
            } catch (Exception e) {
                model.addAttribute("plans", new ArrayList<>());
            }
        }

        private boolean isEmpty(String s) {
            return s == null || s.trim().isEmpty();
        }
    }

}
