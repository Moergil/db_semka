package sk.fri.uniza.db.sem.db.model;

public class Company extends DbRow {

    private final String name;
    private final String dic;
    private final int id;

    public Company(String name, String dic, int id) {
        this.name = name;
        this.dic = dic;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDic() {
        return dic;
    }

    public int getId() {
        return id;
    }

    @Override
    public Object[] getValues() {
        return toRow(name, dic);
    }

}
