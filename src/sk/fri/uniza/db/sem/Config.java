package sk.fri.uniza.db.sem;

import java.text.SimpleDateFormat;

public class Config {

    public static final SimpleDateFormat INPUT_DATE_FORMAT_FULL = new SimpleDateFormat("dd-MM-yyyy");
    public static final SimpleDateFormat INPUT_DATE_FORMAT_YEAR_ONLY = new SimpleDateFormat("yyyy");

    public static final int DATE_MAX_LENGTH = INPUT_DATE_FORMAT_FULL.toPattern().length();

}
