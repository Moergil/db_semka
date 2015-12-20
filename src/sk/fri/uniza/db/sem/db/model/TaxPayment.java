package sk.fri.uniza.db.sem.db.model;

public class TaxPayment extends DbRow {

    private final int taxPayerId;
    private final int year;
    private final float tax;

    public TaxPayment(int taxPayerId, int year, float tax) {
        this.taxPayerId = taxPayerId;
        this.year = year;
        this.tax = tax;
    }

    public int getTaxPayerId() {
        return taxPayerId;
    }

    public int getYear() {
        return year;
    }

    public float getTax() {
        return tax;
    }

    @Override
    public Object[] getValues() {
        return toRow(taxPayerId, year, tax);
    }
}
