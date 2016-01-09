package sk.fri.uniza.db.sem.db;

import oracle.jdbc.OracleTypes;
import sk.fri.uniza.db.sem.PrivateConfig;
import sk.fri.uniza.db.sem.db.model.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OracleSqlDataProvider implements DataProvider, AutoCloseable {

    private static final SelectFunction
            LIST_TAX_PAYING_PERIODS = new SelectFunction(
            "f_du_obdobie_platca",
            OracleTypes.VARCHAR,
            OracleTypes.VARCHAR
    ),
            LIST_INCOME_COMPOSITIONS = new SelectFunction(
                    "f_du_zlozenie_prijmov",
                    OracleTypes.VARCHAR,
                    OracleTypes.VARCHAR,
                    OracleTypes.INTEGER,
                    OracleTypes.INTEGER
            ),
            LIST_TAX_TYPES = new SelectFunction(
                    "f_du_typ_dane"
            ),
            LIST_TAX_PAYERS_WHO_DIDNT_PAYED = new SelectFunction(
                    "f_du_po_neplatca_dane",
                    OracleTypes.INTEGER
            ),
            LIST_ALL_TAX_PAYERS = new SelectFunction(
                    "f_du_zobraz_platcov"
            ),
            LIST_TAX_TYPES_FOR_TAX_PAYER = new SelectFunction(
                    "f_du_zobraz_typ_dane",
                    OracleTypes.INTEGER
            ),
            GET_OFFICE_TOWN_NAME = new SelectFunction(
                    "f_du_vypis_mesto_uradu",
                    OracleTypes.INTEGER
            ),
            LIST_PAYERS_PAY_LESS_THAN_MEAN = new SelectFunction(
                    "f_du_platili_menej_ako_priemer",
                    OracleTypes.INTEGER
            ),
            LIST_COMPANIES = new SelectFunction("f_du_zobraz_firmy"),
            LIST_COMPANIES_OWNERS = new SelectFunction("f_du_zobraz_podnikatelov"),
            LIST_TAXES_OVERVIEW = new SelectFunction(
                    "f_du_zobraz_prehlad_dani",
                    OracleTypes.INTEGER,
                    OracleTypes.INTEGER),
            LIST_INCOME_TAX_WITH_DECLINE = new SelectFunction("f_du_dan_zo_zisku_pokles"),
            SHOW_PAYER_INFO = new SelectFunction(
                    "f_du_zobraz_udaje_platcu",
                    OracleTypes.INTEGER),
            LIST_PAYMENTS = new SelectFunction(
                    "f_du_rozpis_platieb",
                    OracleTypes.INTEGER,
                    OracleTypes.INTEGER,
                    OracleTypes.VARCHAR,
                    OracleTypes.VARCHAR
            ),
            LIST_CHANGES = new SelectFunction(
                    "f_du_vypis_zmien",
                    OracleTypes.VARCHAR,
                    OracleTypes.VARCHAR
            ),
            LIST_TAX_ADVANCES = new SelectFunction("f_du_preddavky_na_dan"),
            LIST_TOP_PAYERS = new SelectFunction(
                    "f_du_zobraz_top_platcov",
                    OracleTypes.INTEGER,
                    OracleTypes.INTEGER
            ),
            LIST_ALLOWED_TAXES = new SelectFunction("f_du_povolene_dane");

    private static final PostProcedure
            POST_NEW_PAYMENT = new PostProcedure(
            "p_du_vloz_platbu",
            OracleTypes.INTEGER,
            OracleTypes.INTEGER,
            OracleTypes.INTEGER,
            OracleTypes.VARCHAR,
            OracleTypes.VARCHAR
            ),
            POST_NEW_TAX_TYPE = new PostProcedure(
                    "p_du_vloz_novy_typ_dane",
                    OracleTypes.VARCHAR
            ),
            POST_TAX_SETTING = new PostProcedure(
                    "p_du_nastav_dan",
                    OracleTypes.INTEGER,
                    OracleTypes.INTEGER,
                    OracleTypes.VARCHAR,
                    OracleTypes.VARCHAR
            ),
            POST_TAX_AFFILIATION = new PostProcedure(
                    "p_du_pridaj_dan_prislusnost",
                    OracleTypes.INTEGER,
                    OracleTypes.INTEGER
            );

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private Connection connection;

    public OracleSqlDataProvider() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void open() throws SQLException {
        String driverType = "thin";
        String databaseName = "asterix.fri.uniza.sk";
        String url = String.format("jdbc:oracle:%s:@%s:1521:ORCL", driverType, databaseName);

        String username = PrivateConfig.DB_USERNAME;
        String password = PrivateConfig.DB_PASSWORD;

        connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public void close() {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String dateToString(Date date) {
        return dateFormat.format(date);
    }

    private Date stringToDate(String textDate) throws ParseException {
        return dateFormat.parse(textDate);
    }

    @Override
    public List<TaxPayingPeriod> listTaxPayingPeriods(Date from, Date to) {
        Mapper<TaxPayingPeriod> mapper = (rs) -> {
            String office = rs.getString(1);
            int month = rs.getInt(2);
            int year = rs.getInt(3);
            int payersCount = rs.getInt(4);

            return new TaxPayingPeriod(office, month, year, payersCount);
        };

        return list(LIST_TAX_PAYING_PERIODS, mapper, dateToString(from), dateToString(to));
    }

    @Override
    public List<IncomeComposition> listIncomeCompositions(Date from, Date to, int taxType, int subjectType) {
        Mapper<IncomeComposition> mapper = (rs) -> {
            int year = rs.getInt(1);
            int month = rs.getInt(2);
            String townName = rs.getString(4);
            float incomeRatio = rs.getFloat(5);

            return new IncomeComposition(year, month, townName, incomeRatio);
        };

        return list(LIST_INCOME_COMPOSITIONS, mapper, dateToString(from), dateToString(to), taxType, subjectType);
    }

    @Override
    public List<TaxType> listTaxTypes() {

        Mapper<TaxType> mapper = (rs) -> {
            int type = rs.getInt(1);
            String name = rs.getString(2);

            return new TaxType(type, name, null);
        };

        return list(LIST_TAX_TYPES, mapper);
    }

    @Override
    public List<TaxType> listTaxTypesForTaxPayer(TaxPayer taxPayer) {
        int taxPayerId = taxPayer.getId();

        Mapper<TaxType> mapper = (rs) -> {
            int type = rs.getInt(1);
            String name = rs.getString(2);
            Date date = stringToDate(rs.getString(4));

            return new TaxType(type, name, date);
        };

        return list(LIST_TAX_TYPES_FOR_TAX_PAYER, mapper, taxPayerId);
    }

    @Override
    public List<TaxPayer> listAllTaxPayers() {
        Mapper<TaxPayer> mapper = (rs) -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);

            return new TaxPayer(id, name);
        };

        return list(LIST_ALL_TAX_PAYERS, mapper);
    }

    @Override
    public List<TaxPayer> listTaxPayersWhoDidntPayed(int taxType) {
        Mapper<TaxPayer> mapper = (rs) -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);

            return new TaxPayer(id, name);
        };

        return list(LIST_TAX_PAYERS_WHO_DIDNT_PAYED, mapper, taxType);
    }

    @Override
    public boolean sentPayment(TaxPayer taxPayer, TaxType taxType, Date created, Date payed, int amount) {
        int idTaxPayer = taxPayer.getId();
        int idTaxType = taxType.getType();
        int amountPayed = amount;
        String datePaymentCreation = dateToString(created);
        String datePaymentPayed = (payed != null) ? dateToString(payed) : null;
        return post(POST_NEW_PAYMENT, idTaxPayer, idTaxType, amountPayed, datePaymentCreation, datePaymentPayed);
    }

    private <T extends DbRow> List<T> list(SelectFunction selectFunction, Mapper<T> mapper, Object... params) {
        try {
            selectFunction.execute(connection, params);
            return selectFunction.map(mapper);
        } catch (Exception e) {
            System.err.println("Response error: " + e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Office getOfficeTownName(int officeId) {
        Mapper<Office> mapper = (rs) -> {
            String officeName = rs.getString(1);
            return new Office(officeName);
        };

        return list(GET_OFFICE_TOWN_NAME, mapper, officeId).get(0);
    }

    @Override
    public List<TaxPayerMean> listPayersWhoPayedLessThanMean(TaxType taxType) {
        Mapper<TaxPayerMean> mapper = (rs) -> {
            int taxPayerId = rs.getInt(1);
            String taxPayerName = rs.getString(2);
            float mean = rs.getFloat(3);
            float meanAll = rs.getFloat(4);
            return new TaxPayerMean(taxPayerId, taxPayerName, mean, meanAll);
        };

        return list(LIST_PAYERS_PAY_LESS_THAN_MEAN, mapper, taxType.getType());
    }

    @Override
    public List<Company> listCompanies() {
        Mapper<Company> mapper = (rs) -> {
            String name = rs.getString(1);
            String dic = rs.getString(2);
            int payerId = rs.getInt(3);

            return new Company(name, dic, payerId);
        };

        return list(LIST_COMPANIES, mapper);
    }

    @Override
    public List<CompanyOwner> listCompaniesOwners() {
        Mapper<CompanyOwner> mapper = (rs) -> {
            String firstname = rs.getString(1);
            String surname = rs.getString(2);
            String dic = rs.getString(3);
            int payerId = rs.getInt(4);

            return new CompanyOwner(firstname, surname, dic, payerId);
        };

        return list(LIST_COMPANIES_OWNERS, mapper);
    }

    @Override
    public List<Payment> listTaxesOverview(TaxPayer taxPayer, TaxType taxType) {
        Mapper<Payment> mapper = (rs) -> {
            int amount = rs.getInt(1);
            Date payedDate = stringToDate(rs.getString(2));

            return new Payment(amount, payedDate);
        };

        int taxPayerId = taxPayer.getId();
        int rawTaxType = taxType.getType();
        return list(LIST_TAXES_OVERVIEW, mapper, taxPayerId, rawTaxType);
    }

    @Override
    public List<TaxPayment> listTaxPaymentsWithDecline() {
        Mapper<TaxPayment> mapper = (rs) -> {
            int taxPayerId = rs.getInt(1);
            String taxPayerName = rs.getString(2);
            int year = rs.getInt(3);
            float tax = rs.getFloat(4);

            return new TaxPayment(taxPayerId, taxPayerName, year, tax);
        };

        return list(LIST_INCOME_TAX_WITH_DECLINE, mapper);
    }

    @Override
    public TaxPayer showTaxPayer(int taxPayer) {
        Mapper<TaxPayer> mapper = (rs) -> {
            int id = rs.getInt(1);
            String name = rs.getString(2);

            return new TaxPayer(id, name);
        };

        return list(SHOW_PAYER_INFO, mapper, taxPayer).get(0);
    }

    @Override
    public List<PaymentSchedule> listPaymentsSchedules(TaxPayer taxPayer, TaxType taxType, Date from, Date to) {
        Mapper<PaymentSchedule> mapper = (rs) -> {
            int taxPayerId = rs.getInt(1);
            int taxTypeId = rs.getInt(2);
            String taxName = rs.getString(3);
            Date creation = stringToDate(rs.getString(4));
            int amount = rs.getInt(5);
            int debt = rs.getInt(6);

            return new PaymentSchedule(taxName, creation, amount, debt);
        };

        int taxPayerId = taxPayer.getId();
        int taxTypeId = taxType.getType();
        String dateFromText = dateToString(from);
        String dateToText = dateToString(to);

        return list(LIST_PAYMENTS, mapper, taxPayerId, taxTypeId, dateFromText, dateToText);
    }

    @Override
    public List<JuridicalPersonName> listChanges(Date from, Date to) {
        Mapper<JuridicalPersonName> mapper = (rs) -> {
            String name = rs.getString(1);

            return new JuridicalPersonName(name);
        };

        return list(LIST_CHANGES, mapper, dateToString(from), dateToString(to));
    }

    @Override
    public List<TaxAdvance> listTaxAdvances() {
        Mapper<TaxAdvance> mapper = (rs) -> {
            int payerId = rs.getInt(1);
            String dic = rs.getString(2);
            String name = rs.getString(3);
            String ico = rs.getString(4);
            int totalTaxDuty = rs.getInt(5);
            float monthAdvancementInNextYear = rs.getFloat(6);

            return new TaxAdvance(payerId, dic, name, ico, totalTaxDuty, monthAdvancementInNextYear);
        };

        return list(LIST_TAX_ADVANCES, mapper);
    }

    @Override
    public List<TopPayer> listTopPayers(int year, TaxType taxType) {
        Mapper<TopPayer> mapper = (rs) -> {
            String taxPayerName = rs.getString(1);
            int taxPayerId = rs.getInt(2);
            int taxTypeId = rs.getInt(3);
            int total = rs.getInt(4);
            int cumDist = rs.getInt(5);

            return new TopPayer(taxPayerName, taxPayerId, taxTypeId, total, cumDist);
        };

        return list(LIST_TOP_PAYERS, mapper, year, taxType.getType());
    }

    @Override
    public boolean addTaxType(String taxType) {
        return post(POST_NEW_TAX_TYPE, taxType);
    }

    @Override
    public boolean setTax(TaxType taxType, int percent, Date validDateFrom, Date validDateTo) {
        int taxTypeInt = taxType.getType();
        String dateFromString = dateToString(validDateFrom);
        String dateToString = dateToString(validDateTo);

        return post(POST_TAX_SETTING, taxTypeInt, percent, dateFromString, dateToString);
    }

    @Override
    public List<TaxType> listAllowedTaxes() {
        Mapper<TaxType> mapper = (rs) -> {
            int type = rs.getInt(1);
            String name = rs.getString(2);

            return new TaxType(type, name, null);
        };

        return list(LIST_ALLOWED_TAXES, mapper);
    }

    @Override
    public boolean setTaxAffiliation(TaxPayer taxPayer, TaxType taxType) {
        int taxPayerId = taxPayer.getId();
        int taxTypeId = taxType.getType();

        return post(POST_TAX_AFFILIATION, taxPayerId, taxTypeId);
    }

    private boolean post(PostProcedure postProcedure, Object... params) {
        try {
            // TODO change to function, return value boolean success/fail
            postProcedure.execute(connection, params);
            return true;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    private static abstract class Call {

        private final String name;
        private final int paramTypes[];

        public Call(String name, int paramTypes[]) {
            this.name = name;
            this.paramTypes = paramTypes;
        }

        public void execute(Connection connection, Object... params) throws SQLException {

            if (params.length != paramTypes.length) {
                String template = "Invalid number of parameters, %d provided, %d expected";
                String message = String.format(template, params.length, paramTypes.length);
                throw new IllegalArgumentException(message);
            }

            String sql = createSql(name, params);

            CallableStatement s = connection.prepareCall(sql);

            onStatementCreated(s);

            int indexOffset = getIndexOffset();
            for (int i = 0; i < paramTypes.length; i++) {
                s.setObject(i + indexOffset, params[i], paramTypes[i]);
            }

            s.execute();

            onStatementExecuted(s);
        }

        protected abstract String createSql(String callName, Object... params);

        protected abstract void onStatementCreated(CallableStatement s) throws SQLException;

        protected abstract void onStatementExecuted(CallableStatement s) throws SQLException;

        protected abstract int getIndexOffset();

        protected String createStringParams(int count) {

            StringBuilder parameters = new StringBuilder();

            for (int i = 0; i < count; i++) {
                if (i != 0) {
                    parameters.append(", ");
                }

                parameters.append("?");
            }

            return parameters.toString();
        }
    }

    private static class SelectFunction<T> extends Call {

        private ResultSet resultSet;

        public SelectFunction(String name, int... paramTypes) {
            super(name, paramTypes);
        }

        @Override
        protected String createSql(String callName, Object... params) {
            String stringParams = createStringParams(params.length);
            return String.format("{? = call %s(%s)}", callName, stringParams);
        }

        @Override
        protected void onStatementCreated(CallableStatement s) throws SQLException {
            s.registerOutParameter(1, OracleTypes.CURSOR);
        }

        @Override
        protected void onStatementExecuted(CallableStatement s) throws SQLException {
            this.resultSet = (ResultSet) s.getObject(1);
        }

        @Override
        protected int getIndexOffset() {
            return 2;
        }

        public List<T> map(Mapper<T> mapper) throws Exception {
            List<T> data = new LinkedList<>();

            while (resultSet.next()) {
                T mapped = mapper.map(resultSet);
                data.add(mapped);
            }
            resultSet.close();

            return data;
        }
    }

    private static class PostProcedure extends Call {

        public PostProcedure(String name, int... paramTypes) {
            super(name, paramTypes);
        }

        @Override
        protected String createSql(String callName, Object... params) {
            String stringParams = createStringParams(params.length);
            return String.format("{call %s(%s)}", callName, stringParams);
        }

        @Override
        protected void onStatementCreated(CallableStatement s) {
        }

        @Override
        protected void onStatementExecuted(CallableStatement s) {
        }

        @Override
        protected int getIndexOffset() {
            return 1;
        }
    }

    private interface Mapper<T> {
        T map(ResultSet rs) throws Exception;
    }
}
