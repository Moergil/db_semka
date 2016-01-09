package sk.fri.uniza.db.sem.db.model;

public class TopPayer extends DbRow {

    private final String taxPayerName;
    private final int taxPayerId;
    private final int taxTypeId;
    private final int total;
    private final int cumDist;

    public TopPayer(String taxPayerName, int taxPayerId, int taxTypeId, int total, int cumDist) {
        this.taxPayerName = taxPayerName;
        this.taxPayerId = taxPayerId;
        this.taxTypeId = taxTypeId;
        this.total = total;
        this.cumDist = cumDist;
    }

    public String getTaxPayerName() {
        return taxPayerName;
    }

    public int getTaxPayerId() {
        return taxPayerId;
    }

    public int getTaxTypeId() {
        return taxTypeId;
    }

    public int getTotal() {
        return total;
    }

    public int getCumDist() {
        return cumDist;
    }

    @Override
    public Object[] getValues() {
        return toRow(taxPayerName, total);
    }
}
