package com.fitness.payment;

public class Cardpayment {package com.fitness.fitnesstrainer.model;

    /**
     * CardPayment.java — Subclass of Payment for card transactions.
     *
     * OOP Concepts:
     *   Inheritance       — extends Payment
     *   Polymorphism      — overrides processPayment() and getPaymentTypeLabel()
     *                       CardPayment describes a card-specific processing method,
     *                       different from CashPayment
     *   Constructor Chain — calls super() to reuse parent constructor
     */
    public class CardPayment extends Payment {

        public CardPayment() {
            super();
            setPaymentType("card");
        }

        public CardPayment(String paymentId, String memberId, String memberName,
                           double amount, String paymentDate, String paymentStatus) {
            super(paymentId, memberId, memberName, amount, paymentDate, paymentStatus, "card");
        }

        // Polymorphism — CardPayment returns card-specific processing info
        // Different output from CashPayment.processPayment()
        @Override
        public String processPayment() {
            return "Card Payment | Processed via POS terminal | Digital receipt sent";
        }

        @Override
        public String getPaymentTypeLabel() {
            return "Card Payment";
        }
    }

}
