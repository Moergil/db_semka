package sk.fri.uniza.db.sem.db.model;

import java.util.Date;

public class PaymentSchedule extends DbRow {

    private final String taxName;
    private final Date creation;
    private final int amount;
    private final int debt;

    public PaymentSchedule(String taxName, Date creation, int amount, int debt) {
        this.taxName = taxName;
        this.creation = creation;
        this.amount = amount;
        this.debt = debt;
    }

    public String getTaxName() {
        return taxName;
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
        return toRow(taxName, creation, amount, debt);
    }
}
