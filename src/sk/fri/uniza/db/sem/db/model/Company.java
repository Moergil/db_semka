package sk.fri.uniza.db.sem.db.model;

public class Company extends DbRow {

    private final String name;
    private final String dic;
    private final int payerId;

    public Company(String name, String dic, int payerId) {
        this.name = name;
        this.dic = dic;
        this.payerId = payerId;
    }

    public String getName() {
        return name;
    }

    public String getDic() {
        return dic;
    }

    public int getPayerId() {
        return payerId;
    }

    @Override
    public Object[] getValues() {
        return toRow(name, dic, payerId);
    }

}
