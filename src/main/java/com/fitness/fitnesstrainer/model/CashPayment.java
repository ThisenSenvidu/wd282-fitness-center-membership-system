package com.fitness.fitnesstrainer.model;

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
