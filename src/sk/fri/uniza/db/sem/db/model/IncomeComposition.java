package sk.fri.uniza.db.sem.db.model;

public class IncomeComposition extends DbRow {

    private final int year;
    private final int month;
    private final String townName;
    private final float incomeRatio;

    public IncomeComposition(int year, int month, String townName, float incomeRatio) {
        this.year = year;
        this.month = month;
        this.townName = townName;
        this.incomeRatio = incomeRatio;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getTownName() {
        return townName;
    }

    public float getIncomeRatio() {
        return incomeRatio;
    }

    @Override
    public Object[] getValues() {
        return toRow(year, month, townName, incomeRatio);
    }

}
