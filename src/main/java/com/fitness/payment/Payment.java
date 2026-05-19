package com.fitness.payment;

public class Payment {package com.fitness.fitnesstrainer.model;

    /**
     * Payment.java — Abstract base class for all payment types.
     *
     * OOP Concepts:
     *   Encapsulation      — private fields with getters/setters
     *   Abstract Class     — cannot be instantiated directly
     *   Abstraction        — abstract method processPayment() forces subclasses
     *                        to define their own payment processing description
     *   Constructor Chain  — no-arg constructor calls full constructor via this()
     *   Static Modifier    — paymentCount tracks total objects created
     */
    public abstract class Payment {

        private String paymentId;
        private String memberId;
        private String memberName;
        private double amount;
        private String paymentDate;
        private String paymentStatus;   // "paid" or "pending"
        private String paymentType;     // "cash" or "card"

        private static int paymentCount = 0;

        // No-arg constructor — Constructor Chaining
        public Payment() {
            this("", "", "", 0.0, "", "", "");
        }

        // Full constructor
        public Payment(String paymentId, String memberId, String memberName,
                       double amount, String paymentDate,
                       String paymentStatus, String paymentType) {
            this.paymentId     = paymentId;
            this.memberId      = memberId;
            this.memberName    = memberName;
            this.amount        = amount;
            this.paymentDate   = paymentDate;
            this.paymentStatus = paymentStatus;
            this.paymentType   = paymentType;
            paymentCount++;
        }

        // Abstract method — Polymorphism
        // Each subclass must describe how their payment is processed
        public abstract String processPayment();

        // Returns a label — overridden by subclasses (Polymorphism)
        public String getPaymentTypeLabel() {
            return "Payment";
        }

        // Static method — Static Modifier
        public static int getPaymentCount() {
            return paymentCount;
        }

        // Save payment as pipe-separated line for payments.txt
        public String toFileString() {
            return paymentId + "|" + memberId + "|" + memberName + "|" +
                    amount + "|" + paymentDate + "|" + paymentStatus + "|" + paymentType;
        }

        // Read one line from payments.txt and return correct subclass (Polymorphism)
        public static Payment fromFileString(String line) {
            String[] parts = line.split("\\|");
            if (parts.length < 7) return null;
            try {
                String paymentId     = parts[0].trim();
                String memberId      = parts[1].trim();
                String memberName    = parts[2].trim();
                double amount        = Double.parseDouble(parts[3].trim());
                String paymentDate   = parts[4].trim();
                String paymentStatus = parts[5].trim();
                String paymentType   = parts[6].trim();

                if (paymentType.equals("card")) {
                    return new CardPayment(paymentId, memberId, memberName,
                            amount, paymentDate, paymentStatus);
                } else {
                    return new CashPayment(paymentId, memberId, memberName,
                            amount, paymentDate, paymentStatus);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        // Formatted amount for display
        public String getFormattedAmount() {
            return String.format("Rs. %.2f", amount);
        }

        // Getters and Setters — Encapsulation
        public String getPaymentId()                         { return paymentId; }
        public void   setPaymentId(String paymentId)         { this.paymentId = paymentId; }

        public String getMemberId()                          { return memberId; }
        public void   setMemberId(String memberId)           { this.memberId = memberId; }

        public String getMemberName()                        { return memberName; }
        public void   setMemberName(String memberName)       { this.memberName = memberName; }

        public double getAmount()                            { return amount; }
        public void   setAmount(double amount)               { this.amount = amount; }

        public String getPaymentDate()                       { return paymentDate; }
        public void   setPaymentDate(String paymentDate)     { this.paymentDate = paymentDate; }

        public String getPaymentStatus()                          { return paymentStatus; }
        public void   setPaymentStatus(String paymentStatus)      { this.paymentStatus = paymentStatus; }

        public String getPaymentType()                       { return paymentType; }
        public void   setPaymentType(String paymentType)     { this.paymentType = paymentType; }

        @Override
        public String toString() {
            return "Payment[id=" + paymentId + ", member=" + memberName +
                    ", amount=" + amount + ", type=" + paymentType + "]";
        }
    }

}
