package sk.fri.uniza.db.sem.db.model;

public class TaxAdvance extends DbRow {

    private final int payerId;
    private final String dic;
    private final String name;
    private final String ico;
    private final int totalTaxDuty;
    private final float monthAdvancementInNextYear;

    public TaxAdvance(int payerId, String dic, String name, String ico, int totalTaxDuty, float monthAdvancementInNextYear) {
        this.payerId = payerId;
        this.dic = dic;
        this.name = name;
        this.ico = ico;
        this.totalTaxDuty = totalTaxDuty;
        this.monthAdvancementInNextYear = monthAdvancementInNextYear;
    }

    public int getPayerId() {
        return payerId;
    }

    public String getDic() {
        return dic;
    }

    public String getName() {
        return name;
    }

    public String getIco() {
        return ico;
    }

    public int getTotalTaxDuty() {
        return totalTaxDuty;
    }

    public float getMonthAdvancementInNextYear() {
        return monthAdvancementInNextYear;
    }

    @Override
    public Object[] getValues() {
        return toRow(payerId, dic, name, ico, totalTaxDuty, monthAdvancementInNextYear);
    }
}
