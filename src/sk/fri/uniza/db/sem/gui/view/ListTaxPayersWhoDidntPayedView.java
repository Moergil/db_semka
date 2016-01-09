package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.Company;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.util.List;

public class ListTaxPayersWhoDidntPayedView extends ProviderTableView<Company, TaxType> {

    private static final String COLUMNS[] = {
            "DIČ Firmy",
            "Názov"
    };

    private final JComboBox<TaxType> taxTypeComboBox;

    public ListTaxPayersWhoDidntPayedView(Application application, String title) {
        super(application, title);

        taxTypeComboBox = new JComboBox<>();

        JToolBar toolbar = createToolbar();
        setToolbar(toolbar);
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(taxTypeComboBox);

        JButton button = new JButton(Strings.SEARCH);
        button.addActionListener((e) -> {
            TaxType params = createParams();

            if (params != null) {
                requestTableDataLoad(params);
            }
        });
        toolbar.add(button);

        return toolbar;
    }

    private TaxType createParams() {
        if (taxTypeComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Daň nebola zvolená.");
            return null;
        }

        return (TaxType) taxTypeComboBox.getSelectedObjects()[0];
    }

    @Override
    public void onShow() {
        super.onShow();
        load(null, this::loadTaxTypes, this::taxTypesLoaded);
    }

    private TaxType[] loadTaxTypes(Void nothing) {
        List<TaxType> taxTypes = getApplication().getDataProvider().listTaxTypes();
        TaxType[] taxTypesArray = new TaxType[taxTypes.size()];
        return taxTypes.toArray(taxTypesArray);
    }

    private void taxTypesLoaded(TaxType[] taxTypes) {
        ComboBoxModel<TaxType> model = new DefaultComboBoxModel<>(taxTypes);
        taxTypeComboBox.setModel(model);

        setLoading(false);
    }

    @Override
    protected List<Company> loadTableDataFromProvider(DataProvider provider, TaxType params) {
        return provider.listTaxPayersWhoDidntPayed(params.getType());
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    protected Object[] mapRow(Company data) {
        return toRow(data.getDic(), data.getName());
    }
}
