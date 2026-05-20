package com.fitness.fitnesstrainer.service;

import com.fitness.fitnesstrainer.model.CardPayment;
import com.fitness.fitnesstrainer.model.CashPayment;
import com.fitness.fitnesstrainer.model.Payment;
import com.fitness.fitnesstrainer.util.PaymentFileHandler;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


@Service
public class PaymentService {

    private final PaymentFileHandler fileHandler;

    public PaymentService(PaymentFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public ArrayList<Payment> getAllPayments() {
        try {
            return fileHandler.getAllPayments();
        } catch (Exception e) {
            throw new RuntimeException("Could not load payments: " + e.getMessage());
        }
    }

    public Payment getPaymentById(String id) {
        try {
            return fileHandler.getPaymentById(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not find payment: " + e.getMessage());
        }
    }

    public ArrayList<Payment> searchPayments(String keyword) {
        try {
            return fileHandler.searchPayments(keyword);
        } catch (Exception e) {
            throw new RuntimeException("Search failed: " + e.getMessage());
        }
    }

    // Polymorphism — creates CashPayment or CardPayment based on paymentType
    public void addPayment(String memberId, String memberName, double amount,
                           String paymentDate, String paymentStatus, String paymentType) {
        Payment payment;
        if ("card".equals(paymentType)) {
            payment = new CardPayment(null, memberId, memberName,
                                      amount, paymentDate, paymentStatus);
        } else {
            payment = new CashPayment(null, memberId, memberName,
                                      amount, paymentDate, paymentStatus);
        }
        try {
            fileHandler.addPayment(payment);
        } catch (Exception e) {
            throw new RuntimeException("Could not save payment: " + e.getMessage());
        }
    }

    public boolean updatePayment(String paymentId, String memberId, String memberName,
                                  double amount, String paymentDate,
                                  String paymentStatus, String paymentType) {
        Payment updated;
        if ("card".equals(paymentType)) {
            updated = new CardPayment(paymentId, memberId, memberName,
                                      amount, paymentDate, paymentStatus);
        } else {
            updated = new CashPayment(paymentId, memberId, memberName,
                                      amount, paymentDate, paymentStatus);
        }
        try {
            return fileHandler.updatePayment(updated);
        } catch (Exception e) {
            throw new RuntimeException("Could not update payment: " + e.getMessage());
        }
    }

    public boolean deletePayment(String id) {
        try {
            return fileHandler.deletePayment(id);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete payment: " + e.getMessage());
        }
    }
}
