package sk.fri.uniza.db.sem.db.model;

public class TaxPayment {

    private final int taxPayerId;
    private final String taxPayerName;
    private final int year;
    private final float tax;

    public TaxPayment(int taxPayerId, String taxPayerName, int year, float tax) {
        this.taxPayerId = taxPayerId;
        this.taxPayerName = taxPayerName;
        this.year = year;
        this.tax = tax;
    }

    public int getTaxPayerId() {
        return taxPayerId;
    }

    public String getTaxPayerName() {
        return taxPayerName;
    }

    public int getYear() {
        return year;
    }

    public float getTax() {
        return tax;
    }

}
