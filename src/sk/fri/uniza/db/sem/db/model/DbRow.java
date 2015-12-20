package sk.fri.uniza.db.sem.db.model;

import oracle.sql.ROWID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class DbRow implements Row {

    protected Object[] toRow(Object... values) {
        return values;
    }

}
