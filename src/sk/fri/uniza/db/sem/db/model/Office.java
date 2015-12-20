package sk.fri.uniza.db.sem.db.model;

public class Office extends DbRow {

    private final String name;

    public Office(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object[] getValues() {
        return toRow(name);
    }
}
