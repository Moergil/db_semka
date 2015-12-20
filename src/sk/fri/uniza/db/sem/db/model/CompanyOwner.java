package sk.fri.uniza.db.sem.db.model;

public class CompanyOwner extends DbRow {

    private final String firstname;
    private final String surname;
    private final String dic;
    private final int payerId;

    public CompanyOwner(String firstname, String surname, String dic, int payerId) {
        this.firstname = firstname;
        this.surname = surname;
        this.dic = dic;
        this.payerId = payerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getDic() {
        return dic;
    }

    public int getPayerId() {
        return payerId;
    }

    @Override
    public Object[] getValues() {
        return toRow(firstname, surname, dic, payerId);
    }
}
