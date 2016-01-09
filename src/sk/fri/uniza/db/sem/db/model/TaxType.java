package sk.fri.uniza.db.sem.db.model;

import java.util.Date;

public class TaxType {

    private final int type;
    private final String name;
    private final Date dateFrom;

    public TaxType(int type, String name, Date dateFrom) {
        this.type = type;
        this.name = name;
        this.dateFrom = dateFrom;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    @Override
    public String toString() {
        return name;
    }
}
