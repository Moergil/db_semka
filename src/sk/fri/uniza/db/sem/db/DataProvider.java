package sk.fri.uniza.db.sem.db;

import sk.fri.uniza.db.sem.db.model.*;

import java.util.Date;
import java.util.List;

public interface DataProvider {

    void open() throws Exception;

    void close();

    List<TaxPayingPeriod> listTaxPayingPeriods(Date from, Date to);

    List<IncomeComposition> listIncomeCompositions(Date from, Date to, int taxType, int subjectType);

    List<TaxType> listTaxTypes();

    List<TaxType> listTaxTypesForTaxPayer(TaxPayer taxPayer);

    List<TaxPayer> listAllTaxPayers();

    List<TaxPayer> listTaxPayersWhoDidntPayed(int taxType);

    boolean sentPayment(TaxPayer taxPayer, TaxType taxType, Date created, Date payed, int amount);

    Office getOfficeTownName(int officeId);

    List<TaxPayerMean> listPayersWhoPayedLessThanMean(TaxType taxType);

    List<Company> listCompanies();

    List<CompanyOwner> listCompaniesOwners();

    List<Payment> listTaxesOverview(TaxPayer taxPayer, TaxType taxType);

    List<TaxPayment> listTaxPaymentsWithDecline();

    TaxPayer showTaxPayer(int taxPayer);

    List<PaymentSchedule> listPaymentsSchedules(TaxPayer taxPayer, TaxType taxType, Date from, Date to);

    List<JuridicalPersonName> listChanges(Date from, Date to);

    List<TaxAdvance> listTaxAdvances();

    List<TopPayer> listTopPayers(int year, TaxType taxType);

    boolean addTaxType(String taxType);

    boolean setTax(TaxType taxType, int percent, Date validDateFrom, Date validToFrom);

    List<TaxType> listAllowedTaxes();

    boolean setTaxAffiliation(TaxPayer taxPayer, TaxType taxType);

}
