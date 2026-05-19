package com.fitness.payment;

public class CashPayment {package com.fitness.fitnesstrainer.model;

    /**
     * CashPayment.java — Subclass of Payment for cash transactions.
     *
     * OOP Concepts:
     *   Inheritance       — extends Payment
     *   Polymorphism      — overrides processPayment() and getPaymentTypeLabel()
     *                       CashPayment describes a cash-specific processing method
     *   Constructor Chain — calls super() to reuse parent constructor
     */
    public class CashPayment extends Payment {

        public CashPayment() {
            super();
            setPaymentType("cash");
        }

        public CashPayment(String paymentId, String memberId, String memberName,
                           double amount, String paymentDate, String paymentStatus) {
            super(paymentId, memberId, memberName, amount, paymentDate, paymentStatus, "cash");
        }

        // Polymorphism — CashPayment returns cash-specific processing info
        @Override
        public String processPayment() {
            return "Cash Payment | Collected at front desk | Receipt issued manually";
        }

        @Override
        public String getPaymentTypeLabel() {
            return "Cash Payment";
        }
    }
}
