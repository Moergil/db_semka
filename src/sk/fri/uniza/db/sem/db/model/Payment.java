package sk.fri.uniza.db.sem.db.model;

import java.util.Date;

public class Payment extends DbRow {

    private final int amount;
    private final Date payedDate;

    public Payment(int amount, Date payedDate) {
        this.amount = amount;
        this.payedDate = payedDate;
    }

    public int getAmount() {
        return amount;
    }

    public Date getPayedDate() {
        return payedDate;
    }

    @Override
    public Object[] getValues() {
        return toRow(amount, payedDate);
    }

}
