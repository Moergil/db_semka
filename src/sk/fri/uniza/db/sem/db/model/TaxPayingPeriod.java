package sk.fri.uniza.db.sem.db.model;

public class TaxPayingPeriod {

    private final String office;
    private final int month;
    private final int year;
    private final int payersCount;

    public TaxPayingPeriod(String office, int month, int year, int payersCount) {
        this.office = office;
        this.month = month;
        this.year = year;
        this.payersCount = payersCount;
    }

    public String getOffice() {
        return office;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getPayersCount() {
        return payersCount;
    }

}
