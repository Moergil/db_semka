package sk.fri.uniza.db.sem.db.model;

public class TopPayer extends DbRow {

    private final TaxPayer[] taxPayers;
    private int payerId;
    private int taxType;
    private int together;

    public TopPayer(TaxPayer[] taxPayers, int payerId, int taxType, int together) {
        this.taxPayers = taxPayers;
        this.payerId = payerId;
        this.taxType = taxType;
        this.together = together;
    }

    public TaxPayer[] getTaxPayers() {
        return taxPayers;
    }

    public int getPayerId() {
        return payerId;
    }

    public int getTaxType() {
        return taxType;
    }

    public int getTogether() {
        return together;
    }

    @Override
    public Object[] getValues() {
        return toRow(taxPayers, payerId, taxType, together);
    }
}
