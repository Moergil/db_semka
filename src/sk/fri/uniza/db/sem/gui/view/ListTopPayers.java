package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.db.model.TopPayer;
import sk.fri.uniza.db.sem.util.DataWorker;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ListTopPayers extends ProviderTableView<TopPayer, ListTopPayers.TopPayerParams> {

    private static final String COLUMNS[] = {
            "Platca",
            "Typ dane",
            "Spolu"
    };

    private final JTextField yearField;
    private final JComboBox<TaxType> taxTypesComboBox;

    private DataWorker<Void, TaxType[]> taxTypesLoader;

    public ListTopPayers(Application application) {
        super(application);

        this.yearField = new JTextField(4);
        this.taxTypesComboBox = new JComboBox<>();

        JToolBar toolbar = createToolbar();
        setToolbar(toolbar);
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        JLabel yearLabel = new JLabel(Strings.YEAR);

        c.gridx = 0;
        c.gridy = 0;
        toolbar.add(yearLabel, c);

        c.gridx++;
        toolbar.add(yearField, c);

        c.gridx++;
        JLabel taxTypeLabel = new JLabel(Strings.TAX_TYPE);
        toolbar.add(taxTypeLabel, c);

        c.gridx++;
        toolbar.add(taxTypesComboBox, c);

        c.gridx++;
        JButton button = new JButton(Strings.SEARCH);
        button.addActionListener((e) -> {
            TopPayerParams params = createParams();

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

        taxTypesLoader = new DataWorker<>(null, this::loadTaxTypes, this::onTaxTypesLoaded);
        taxTypesLoader.execute();

        setLoading(true);
    }

    @Override
    protected List<TopPayer> loadTableDataFromProvider(DataProvider provider, TopPayerParams params) {
        int year = params.getYear();
        TaxType taxType = params.getTaxType();
        return provider.listTopPayers(year, taxType);
    }

    private TaxType[] loadTaxTypes(Void nothing) {
        List<TaxType> taxTypesList = getApplication().getDataProvider().listTaxTypes();
        TaxType[] taxTypeArray = new TaxType[taxTypesList.size()];
        return taxTypesList.toArray(taxTypeArray);
    }

    private void onTaxTypesLoaded(TaxType[] taxTypes) {
        ComboBoxModel<TaxType> model = new DefaultComboBoxModel<>(taxTypes);
        taxTypesComboBox.setModel(model);

        setLoading(false);
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    public String getTitle() {
        return "Top platci";
    }

    private TopPayerParams createParams() {
        try {
            String yearText = yearField.getText();
            int year = InputParser.parseYear(yearText, 1900, 2020); // TODO hardcoded

            TaxType taxType = (TaxType) taxTypesComboBox.getSelectedObjects()[0];

            return new TopPayerParams(year, taxType);
        } catch (Exception e) {
            return null;
        }
    }

    protected static class TopPayerParams {

        private final int year;
        private final TaxType taxType;

        public TopPayerParams(int year, TaxType taxType) {
            this.year = year;
            this.taxType = taxType;
        }

        public int getYear() {
            return year;
        }

        public TaxType getTaxType() {
            return taxType;
        }
    }
}
