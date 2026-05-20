package com.fitness.fitnesstrainer.util;

import com.fitness.fitnesstrainer.model.Payment;
import com.fitness.fitnesstrainer.model.CashPayment;
import com.fitness.fitnesstrainer.model.CardPayment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;


@Component
public class PaymentFileHandler {

    @Value("${app.payments.file-path}")
    private String filePath;

    @PostConstruct
    public void initFile() {
        try {
            File file = new File(filePath);
            File dir  = file.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            if (!file.exists())               file.createNewFile();
        } catch (IOException e) {
            System.out.println("Warning: " + e.getMessage());
        }
    }

    // CREATE — append one new payment line to payments.txt
    public void addPayment(Payment payment) throws IOException {
        String newId = generateId();
        payment.setPaymentId(newId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(payment.toFileString());
            writer.newLine();
        }
    }

    // READ ALL — return every payment as ArrayList<Payment>
    public ArrayList<Payment> getAllPayments() throws IOException {
        ArrayList<Payment> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Payment p = Payment.fromFileString(line); // Polymorphism
                    if (p != null) list.add(p);
                }
            }
        }
        return list;
    }

    // READ BY ID — find one payment by its ID
    public Payment getPaymentById(String paymentId) throws IOException {
        for (Payment p : getAllPayments()) {
            if (p.getPaymentId().equals(paymentId)) return p;
        }
        return null;
    }

    // SEARCH — filter by member name or member ID
    public ArrayList<Payment> searchPayments(String keyword) throws IOException {
        ArrayList<Payment> results = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        for (Payment p : getAllPayments()) {
            if (p.getMemberName().toLowerCase().contains(kw) ||
                p.getMemberId().toLowerCase().contains(kw)) {
                results.add(p);
            }
        }
        return results;
    }

    // UPDATE — find matching payment, replace it, rewrite file
    public boolean updatePayment(Payment updated) throws IOException {
        ArrayList<Payment> all = getAllPayments();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPaymentId().equals(updated.getPaymentId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // DELETE — remove payment with matching ID, rewrite file
    public boolean deletePayment(String paymentId) throws IOException {
        ArrayList<Payment> all = getAllPayments();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPaymentId().equals(paymentId)) {
                all.remove(i);
                found = true;
                break;
            }
        }
        if (found) writeAllToFile(all);
        return found;
    }

    // Helper — overwrite entire file with updated list
    private void writeAllToFile(ArrayList<Payment> payments) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Payment p : payments) {
                writer.write(p.toFileString());
                writer.newLine();
            }
        }
    }

    // Helper — generate ID like PAY-4001, PAY-4002
    private String generateId() throws IOException {
        int nextNum = 4001 + getAllPayments().size();
        return String.format("PAY-%d", nextNum);
    }
}
