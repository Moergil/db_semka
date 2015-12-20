package sk.fri.uniza.db.sem.util;

import sk.fri.uniza.db.sem.Config;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;

public class InputParser {

    public static Date parseDate(String value) throws ParseException {

        DateFormat formatters[] = {
                Config.INPUT_DATE_FORMAT_FULL,
                Config.INPUT_DATE_FORMAT_YEAR_ONLY
        };

        for (DateFormat formatter : formatters) {
            try {
                Date parsedDate = formatter.parse(value);
                return parsedDate;
            } catch (ParseException e) {
            }
        }

        JOptionPane.showMessageDialog(null, "Nesprávny formát dátumu (yyyy alebo dd-mm-yyyy): " + value);

        throw new ParseException("Can not parseDate provided date " + value, 0);
    }

    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Zadaná hodnota nieje celé číslo: " + value);
            throw e;
        }
    }

    public static int parseYear(String value, int min, int max) {
        int number = Integer.parseInt(value);

        if (number < min || number > max) {
            String message = String.format("Zadaný rok je mimo rozsah (%d až %d vrátane)", min, max);
            JOptionPane.showMessageDialog(null, message);
            throw new NumberFormatException("Year is out of range.");
        }

        return number;
    }

    public static <T> T getFirstSelectedItem(Object[] items) {
        if (items == null || items.length == 0) {
            JOptionPane.showMessageDialog(null, "Nieje zvolený objekt zo zoznamu.");
            throw new NoSuchElementException("Items are null or empty.");
        }

        return (T)items[0];
    }

}
