package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.Payment;
import sk.fri.uniza.db.sem.db.model.TaxPayer;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.DataWorker;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListTaxesOverview extends ProviderTableView<Payment, ListTaxesOverview.TaxesOverviewParams> {

    private static final String[] COLUMNS = {
            "Suma",
            "Dátum zaplatenia"
    };

    private static final int PAYERS_LOADED = 1;
    private static final int TAXES_LOADED = 1 << 1;

    private JComboBox<TaxPayer> taxPayersComboBox;
    private JComboBox<TaxType> taxTypesComboBox;

    private DataWorker<Void, TaxPayer[]> taxPayerLoader;
    private DataWorker<Void, TaxType[]> taxTypesLoader;

    private int loadingMask;

    public ListTaxesOverview(Application application) {
        super(application);

        taxPayersComboBox = new JComboBox<>();
        taxTypesComboBox = new JComboBox<>();

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
        JLabel taxPayerLabel = new JLabel(Strings.TAX_PAYER);
        toolbar.add(taxPayerLabel, c);

        c.gridx++;
        toolbar.add(taxPayersComboBox, c);

        c.gridx = 0;
        c.gridy = 1;
        JLabel taxTypeLabel = new JLabel(Strings.DATO_TO);
        toolbar.add(taxTypeLabel, c);

        c.gridx++;
        toolbar.add(taxTypesComboBox, c);

        c.gridx++;
        c.gridy = 0;
        c.gridheight = 2;
        JButton button = new JButton(Strings.SEARCH);
        button.addActionListener((e) -> {
            TaxesOverviewParams params = createParams();

            if (params != null) {
                requestTableDataLoad(params);
            }
        });
        toolbar.add(button, c);

        return toolbar;
    }

    @Override
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
        taxPayersComboBox.setModel(model);

        onComboBoxLoaded(PAYERS_LOADED);
    }

    private TaxType[] loadTaxTypes(Void nothing) {
        List<TaxType> taxTypesList = getApplication().getDataProvider().listTaxTypes();
        TaxType[] taxTypeArray = new TaxType[taxTypesList.size()];
        return taxTypesList.toArray(taxTypeArray);
    }

    private void onTaxTypesLoaded(TaxType[] taxTypes) {
        ComboBoxModel<TaxType> model = new DefaultComboBoxModel<>(taxTypes);
        taxTypesComboBox.setModel(model);

        onComboBoxLoaded(TAXES_LOADED);
    }

    private void onComboBoxLoaded(int mask) {
        loadingMask &= ~mask;

        if (loadingMask == 0) {
            setLoading(false);
        }
    }

    private TaxesOverviewParams createParams() {
        try {
            Object[] selectedTaxPayers = taxPayersComboBox.getSelectedObjects();
            TaxPayer taxPayer = InputParser.getFirstSelectedItem(selectedTaxPayers);

            Object[] selectedTaxTypes = taxTypesComboBox.getSelectedObjects();
            TaxType taxType = InputParser.getFirstSelectedItem(selectedTaxTypes);

            return new TaxesOverviewParams(taxPayer, taxType);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected List<Payment> loadTableDataFromProvider(DataProvider provider, TaxesOverviewParams params) {
        TaxPayer taxPayer = params.getTaxPayer();
        TaxType taxType = params.getTaxType();

        return provider.listTaxesOverview(taxPayer, taxType);
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    public String getTitle() {
        return "Prehľad daní";
    }

    protected static class TaxesOverviewParams {

        private final TaxPayer taxPayer;
        private final TaxType taxType;

        public TaxesOverviewParams(TaxPayer taxPayer, TaxType taxType) {
            this.taxPayer = taxPayer;
            this.taxType = taxType;
        }

        public TaxPayer getTaxPayer() {
            return taxPayer;
        }

        public TaxType getTaxType() {
            return taxType;
        }
    }

}
