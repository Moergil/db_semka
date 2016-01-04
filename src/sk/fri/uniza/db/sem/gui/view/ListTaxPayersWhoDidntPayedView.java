package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxPayer;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.DataWorker;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.util.List;

public class ListTaxPayersWhoDidntPayedView extends ProviderTableView<TaxPayer, TaxType> {

    private static final String COLUMNS[] = {
            "DIČ Firmy",
            "Názov"
    };

    private final JComboBox<TaxType> taxTypeComboBox;

    private DataWorker<Void, TaxType[]> taxTypesLoader;

    public ListTaxPayersWhoDidntPayedView(Application application) {
        super(application);

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
            requestTableDataLoad(params);
        });
        toolbar.add(button);

        return toolbar;
    }

    private TaxType createParams() {
        return (TaxType) taxTypeComboBox.getSelectedObjects()[0];
    }

    @Override
    public void onShow() {
        super.onShow();

        taxTypesLoader = new DataWorker<>(null, this::loadTaxTypes, this::taxTypesLoaded);
        taxTypesLoader.execute();

        setLoading(true);
    }

    @Override
    public void onHide() {
        super.onHide();

        if (taxTypesLoader != null) {
            taxTypesLoader.cancel(true);
        }
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
    protected List<TaxPayer> loadTableDataFromProvider(DataProvider provider, TaxType params) {
        return provider.listTaxPayersWhoDidntPayed(params.getType());
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    public String getTitle() {
        return "Neplatiči Za Posledné Dva Roky";
    }
}
