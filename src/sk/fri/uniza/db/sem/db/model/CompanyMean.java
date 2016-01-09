package sk.fri.uniza.db.sem.db.model;

public class CompanyMean {

    private final String dic;
    private final String name;
    private final float mean;
    private final float meanOthers;

    public CompanyMean(String dic, String name, float mean, float meanOthers) {
        this.dic = dic;
        this.name = name;
        this.mean = mean;
        this.meanOthers = meanOthers;
    }

    public String getDic() {
        return dic;
    }

    public String getName() {
        return name;
    }

    public float getMean() {
        return mean;
    }

    public float getMeanOthers() {
        return meanOthers;
    }

}
