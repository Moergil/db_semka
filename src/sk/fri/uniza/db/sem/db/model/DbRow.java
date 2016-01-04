package sk.fri.uniza.db.sem.db.model;

public abstract class DbRow implements Row {

    protected Object[] toRow(Object... values) {
        return values;
    }

}
