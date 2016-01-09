package sk.fri.uniza.db.sem.db.model;

import java.util.Date;

public class Penalty {

    private final String payerName;
    private final Date dateCreated;
    private final String paymentState;
    private final int amount;
    private final int percent;
    private final int payerId;
    private final float penalty;

    public Penalty(String payerName, Date dateCreated, String paymentState, int amount, int percent, int payerId, float penalty) {
        this.payerName = payerName;
        this.dateCreated = dateCreated;
        this.paymentState = paymentState;
        this.amount = amount;
        this.percent = percent;
        this.payerId = payerId;
        this.penalty = penalty;
    }

    public String getPayerName() {
        return payerName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public int getAmount() {
        return amount;
    }

    public int getPercent() {
        return percent;
    }

    public int getPayerId() {
        return payerId;
    }

    public float getPenalty() {
        return penalty;
    }

}
