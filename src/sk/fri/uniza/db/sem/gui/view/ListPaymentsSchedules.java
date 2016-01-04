package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.Config;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.PaymentSchedule;
import sk.fri.uniza.db.sem.db.model.TaxPayer;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.DataWorker;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class ListPaymentsSchedules extends ProviderTableView<PaymentSchedule, ListPaymentsSchedules.PaymentSchedulesParams> {

    private static final String COLUMNS[] = {
            "<<id platcu>>",
            "<<typ dane>>",
            "Dátum vzniku",
            "Suma",
            "Dlh"
    };

    private static final int PAYERS_LOADED = 1;
    private static final int TAXES_LOADED = 1 << 1;

    private final JComboBox<TaxPayer> taxPayerComboBox;
    private final JComboBox<TaxType> taxTypeComboBox;

    private final JTextField dateFromField;
    private final JTextField dateToField;

    private DataWorker<Void, TaxPayer[]> taxPayerLoader;
    private DataWorker<Void, TaxType[]> taxTypesLoader;

    private int loadingMask;

    public ListPaymentsSchedules(Application application) {
        super(application);

        this.taxPayerComboBox = new JComboBox<>();
        this.taxTypeComboBox = new JComboBox<>();

        this.dateFromField = new JTextField(Config.DATE_MAX_LENGHT);
        this.dateToField = new JTextField(Config.DATE_MAX_LENGHT);

        JToolBar toolbar = createToolbar();
        setToolbar(toolbar);
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JLabel taxTypeLabel = new JLabel(Strings.TAX_PAYER);
        toolbar.add(taxTypeLabel, c);

        c.gridx += 2;
        toolbar.add(taxPayerComboBox, c);

        c.gridx = 0;
        c.gridy++;
        JLabel subjectTypeLable = new JLabel(Strings.TAX_TYPE);
        toolbar.add(subjectTypeLable, c);

        c.gridx += 2;
        toolbar.add(taxTypeComboBox, c);

        JLabel dateFromLabel = new JLabel(Strings.DATE_FROM);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 1;
        toolbar.add(dateFromLabel, c);

        c.gridx++;
        toolbar.add(dateFromField, c);

        c.gridx++;
        JLabel dateToLabel = new JLabel(Strings.DATO_TO);
        toolbar.add(dateToLabel, c);

        c.gridx++;
        toolbar.add(dateToField, c);

        c.gridx = 5;
        c.gridy = 0;
        c.gridheight = 3;
        JButton button = new JButton(Strings.SEARCH);
        button.addActionListener((e) -> {
            PaymentSchedulesParams params = createParams();

            if (params != null) {
                requestTableDataLoad(params);
            }
        });
        toolbar.add(button, c);

        return toolbar;
    }

    public void onShow() {
        super.onShow();

        loadingMask = PAYERS_LOADED | TAXES_LOADED;

        taxPayerLoader = new DataWorker<>(null, this::loadTaxPayers, this::onTaxPayersLoaded);
        taxPayerLoader.execute();

        taxTypesLoader = new DataWorker<>(null, this::loadTaxTypes, this::onTaxTypesLoaded);
        taxTypesLoader.execute();

        setLoading(true);
    }

    private TaxPayer[] loadTaxPayers(Void nothing) {
        List<TaxPayer> taxPayers = getApplication().getDataProvider().listAllTaxPayers();
        TaxPayer[] taxPayersArray = new TaxPayer[taxPayers.size()];
        return taxPayers.toArray(taxPayersArray);
    }

    private void onTaxPayersLoaded(TaxPayer[] taxPayers) {
        ComboBoxModel<TaxPayer> model = new DefaultComboBoxModel<>(taxPayers);
        taxPayerComboBox.setModel(model);

        onComboBoxLoaded(PAYERS_LOADED);
    }

    private TaxType[] loadTaxTypes(Void nothing) {
        List<TaxType> taxTypesList = getApplication().getDataProvider().listTaxTypes();
        TaxType[] taxTypeArray = new TaxType[taxTypesList.size()];
        return taxTypesList.toArray(taxTypeArray);
    }

    private void onTaxTypesLoaded(TaxType[] taxTypes) {
        ComboBoxModel<TaxType> model = new DefaultComboBoxModel<>(taxTypes);
        taxTypeComboBox.setModel(model);

        onComboBoxLoaded(TAXES_LOADED);
    }

    private void onComboBoxLoaded(int mask) {
        loadingMask &= ~mask;

        if (loadingMask == 0) {
            setLoading(false);
        }
    }

    @Override
    protected List<PaymentSchedule> loadTableDataFromProvider(DataProvider provider, PaymentSchedulesParams params) {
        TaxPayer taxPayer = params.getTaxPayer();
        TaxType taxType = params.getTaxType();
        Date dateFrom = params.getDateFrom();
        Date dateTo = params.getDateTo();

        return provider.listPaymentsSchedules(taxPayer, taxType, dateFrom, dateTo);
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    public String getTitle() {
        return "Rozpis platieb";
    }

    private PaymentSchedulesParams createParams() {
        try {
            TaxPayer taxPayer = InputParser.getFirstSelectedItem(taxPayerComboBox.getSelectedObjects());
            TaxType taxType = InputParser.getFirstSelectedItem(taxTypeComboBox.getSelectedObjects());

            String dateFromText = dateFromField.getText();
            Date dateFrom = InputParser.parseDate(dateFromText);

            String dateToText = dateToField.getText();
            Date dateTo = InputParser.parseDate(dateToText);

            return new PaymentSchedulesParams(taxPayer, taxType, dateFrom, dateTo);
        } catch (Exception e) {
            return null;
        }
    }

    protected static class PaymentSchedulesParams {

        private final TaxPayer taxPayer;
        private final TaxType taxType;
        private final Date dateFrom;
        private final Date dateTo;

        public PaymentSchedulesParams(TaxPayer taxPayer, TaxType taxType, Date dateFrom, Date dateTo) {
            this.taxPayer = taxPayer;
            this.taxType = taxType;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
        }

        public TaxPayer getTaxPayer() {
            return taxPayer;
        }

        public TaxType getTaxType() {
            return taxType;
        }

        public Date getDateFrom() {
            return dateFrom;
        }

        public Date getDateTo() {
            return dateTo;
        }
    }
}
