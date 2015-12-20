package sk.fri.uniza.db.sem.db.model;

public class TaxPayer extends DbRow {

    private final int id;
    private final String name;

    public TaxPayer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object[] getValues() {
        return toRow(id, name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
