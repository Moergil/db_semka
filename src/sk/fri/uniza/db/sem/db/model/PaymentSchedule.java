package sk.fri.uniza.db.sem.db.model;

import java.util.Date;

public class PaymentSchedule extends DbRow {

    private final int payerId;
    private final int taxType;
    private final Date creation;
    private final int amount;
    private final int debt;

    public PaymentSchedule(int payerId, int taxType, Date creation, int amount, int debt) {
        this.payerId = payerId;
        this.taxType = taxType;
        this.creation = creation;
        this.amount = amount;
        this.debt = debt;
    }

    public int getPayerId() {
        return payerId;
    }

    public int getTaxType() {
        return taxType;
    }

    public Date getCreation() {
        return creation;
    }

    public int getAmount() {
        return amount;
    }

    public int getDebt() {
        return debt;
    }

    @Override
    public Object[] getValues() {
        return new Object[0];
    }
}
