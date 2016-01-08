package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxPayer;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TaxAffiliationView extends FormView {

    private static final int LOADING_TAX_PAYERS = 1 << 0;
    private static final int LOADING_TAX_TYPES = 1 << 1;

    private final JPanel contentComponent;

    private final JComboBox<TaxPayer> taxPayerComboBox;
    private final JComboBox<TaxType> taxTypeComboBox;

    private int loadedMap;

    public TaxAffiliationView(Application application) {
        super(application);

        taxPayerComboBox = new JComboBox<>();
        taxTypeComboBox = new JComboBox<>();

        contentComponent = new JPanel();
        contentComponent.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;

        JLabel taxPayerNameLabel = new JLabel(Strings.TAX_PAYER);
        contentComponent.add(taxPayerNameLabel, c);

        c.gridx++;

        contentComponent.add(taxPayerComboBox, c);

        c.gridx = 0;
        c.gridy++;

        JLabel taxTypeNameLabel = new JLabel(Strings.TAX_TYPE);
        contentComponent.add(taxTypeNameLabel, c);

        c.gridx++;

        contentComponent.add(taxTypeComboBox, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;

        JButton submitButton = new JButton(Strings.ADD);
        submitButton.addActionListener((e) -> processForm());
        contentComponent.add(submitButton, c);

        setContentComponent(contentComponent);
    }

    @Override
    public void onShow() {
        loadedMap = 0;
        load(null, this::loadTaxPayers, this::onTaxPayersLoaded);
        load(null, this::loadTaxTypes, this::onTaxTypesLoaded);
    }

    private List<TaxPayer> loadTaxPayers(Void nothing) {
        DataProvider provider = getApplication().getDataProvider();
        return provider.listAllTaxPayers();
    }

    private void onTaxPayersLoaded(List<TaxPayer> taxPayers) {
        TaxPayer[] taxPayersArray = new TaxPayer[taxPayers.size()];
        taxPayers.toArray(taxPayersArray);

        ComboBoxModel<TaxPayer> model = new DefaultComboBoxModel<>(taxPayersArray);
        taxPayerComboBox.setModel(model);

        updateLoading(LOADING_TAX_PAYERS);
    }

    private List<TaxType> loadTaxTypes(Void nothing) {
        DataProvider provider = getApplication().getDataProvider();
        return provider.listTaxTypes();
    }

    private void onTaxTypesLoaded(List<TaxType> taxTypes) {
        TaxType[] taxTypesArray = new TaxType[taxTypes.size()];
        taxTypes.toArray(taxTypesArray);

        ComboBoxModel<TaxType> model = new DefaultComboBoxModel<>(taxTypesArray);
        taxTypeComboBox.setModel(model);

        updateLoading(LOADING_TAX_TYPES);
    }

    private void updateLoading(int value) {
        loadedMap |= value;

        if (loadedMap == (LOADING_TAX_PAYERS | LOADING_TAX_TYPES)) {
            setLoading(false);
        }
    }

    private void processForm() {
        if (taxPayerComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Platca dane nieje zvolený.");
            return;
        }

        TaxPayer taxPayer = (TaxPayer) taxPayerComboBox.getSelectedObjects()[0];

        if (taxTypeComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Typ dane nieje zvolený.");
            return;
        }

        TaxType taxType = (TaxType) taxTypeComboBox.getSelectedObjects()[0];

        SendParams params = new SendParams(taxPayer, taxType);

        load(params, this::sendForm, this::onFormSent);
    }

    private boolean sendForm(SendParams params) {
        DataProvider provider = getApplication().getDataProvider();

        TaxPayer taxPayer = params.getTaxPayer();
        TaxType taxType = params.getTaxType();

        return provider.setTaxAffiliation(taxPayer, taxType);
    }

    private void onFormSent(boolean result) {
        String message;
        if (result) {
            message = "Príslušnosť dane bola nastavená.";
        } else {
            message = "Nastavenie príslušnosti dane zlyhalo.";
        }

        JOptionPane.showMessageDialog(null, message);

        setLoading(false);
    }

    @Override
    public String getTitle() {
        return "Daňová príslušnoť";
    }

    private static class SendParams {

        private final TaxPayer taxPayer;
        private final TaxType taxType;

        public SendParams(TaxPayer taxPayer, TaxType taxType) {
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
