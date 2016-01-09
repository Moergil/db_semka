package sk.fri.uniza.db.sem.db.model;

public class TaxPayerMean {

    private final int taxPayerId;
    private final String taxPayerName;
    private final float mean;
    private final float meanOthers;

    public TaxPayerMean(int taxPayerId, String taxPayerName, float mean, float meanOthers) {
        this.taxPayerId = taxPayerId;
        this.taxPayerName = taxPayerName;
        this.mean = mean;
        this.meanOthers = meanOthers;
    }

    public int getTaxPayerId() {
        return taxPayerId;
    }

    public String getTaxPayerName() {
        return taxPayerName;
    }

    public float getMean() {
        return mean;
    }

    public float getMeanOthers() {
        return meanOthers;
    }

}
