package sk.fri.uniza.db.sem.db.model;

public class SubjectType extends DbRow {

    private final int type;
    private final String name;

    public SubjectType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object[] getValues() {
        return toRow(type, name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
