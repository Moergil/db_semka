package sk.fri.uniza.db.sem.db.model;

public class TaxPayer {

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
    public String toString() {
        return name;
    }
}
