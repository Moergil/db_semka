package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.Config;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxPayer;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class NewPaymentView extends FormView {

    private final JPanel contentComponent;

    private final JComboBox<TaxPayer> taxPayerComboBox;
    private final JComboBox<TaxType> taxTypeComboBox;

    private final JTextField datePaymentCreationField;
    private final JTextField datePaymentPayedField;
    private final JTextField amountField;

    private final JButton submitButton;

    public NewPaymentView(Application application) {
        super(application);

        contentComponent = new JPanel();
        contentComponent.setLayout(new GridBagLayout());

        taxPayerComboBox = new JComboBox<>();
        taxPayerComboBox.addActionListener((e) -> {
            TaxPayer selected = (TaxPayer) taxPayerComboBox.getSelectedObjects()[0];
            load(selected, this::loadTaxTypesForTaxPayer, this::onTaxTypesForTaxPayerLoaded);
        });
        taxTypeComboBox = new JComboBox<>();

        int dateInputColumns = Config.DATE_MAX_LENGTH;
        datePaymentCreationField = new JTextField(dateInputColumns);
        datePaymentPayedField = new JTextField(dateInputColumns);
        amountField = new JTextField(10);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;

        JLabel taxPayer = new JLabel(Strings.TAX_PAYER);
        contentComponent.add(taxPayer, c);

        c.gridx++;

        contentComponent.add(taxPayerComboBox, c);

        c.gridx++;

        JLabel taxTypeLabel = new JLabel(Strings.TAX_TYPE);
        contentComponent.add(taxTypeLabel, c);

        c.gridx++;

        contentComponent.add(taxTypeComboBox, c);

        c.gridx = 0;
        c.gridy++;

        JLabel datePaymentCreationLabel = new JLabel(Strings.PAYMENT_CREATION_DATE);
        contentComponent.add(datePaymentCreationLabel, c);

        c.gridx++;

        contentComponent.add(datePaymentCreationField, c);

        c.gridx++;

        JLabel datePaymentPayedLabel = new JLabel(Strings.PAYMENT_PAYED_DATE);
        contentComponent.add(datePaymentPayedLabel, c);

        c.gridx++;

        contentComponent.add(datePaymentPayedField, c);

        c.gridx = 0;
        c.gridy++;

        JLabel amountToPayLabel = new JLabel(Strings.AMOUNT_TO_PAY);
        contentComponent.add(amountToPayLabel, c);

        c.gridx++;

        contentComponent.add(amountField, c);

        c.gridx++;
        c.gridwidth = 2;

        submitButton = new JButton(Strings.PAY);
        submitButton.addActionListener((e) -> processForm());
        submitButton.setEnabled(false);
        contentComponent.add(submitButton, c);

        setContentComponent(contentComponent);
    }

    @Override
    public String getTitle() {
        return "Nová platba";
    }

    @Override
    public void onShow() {
        load(null, this::loadTaxPayers, this::onTaxPayersLoaded);
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

        if (!taxPayers.isEmpty()) {
            TaxPayer first = (TaxPayer) taxPayerComboBox.getSelectedObjects()[0];
            load(first, this::loadTaxTypesForTaxPayer, this::onTaxTypesForTaxPayerLoaded);
        } else {
            setLoading(false);
        }
    }

    private List<TaxType> loadTaxTypesForTaxPayer(TaxPayer taxPayer) {
        DataProvider provider = getApplication().getDataProvider();
        return provider.listTaxTypesForTaxPayer(taxPayer);
    }

    private void onTaxTypesForTaxPayerLoaded(List<TaxType> taxTypes) {
        TaxType[] taxTypesArray = new TaxType[taxTypes.size()];
        taxTypes.toArray(taxTypesArray);

        ComboBoxModel<TaxType> model = new DefaultComboBoxModel<>(taxTypesArray);
        taxTypeComboBox.setModel(model);

        boolean enableSubmit = !taxTypes.isEmpty();
        submitButton.setEnabled(enableSubmit);

        boolean enableComboBox = !taxTypes.isEmpty();
        taxTypeComboBox.setEnabled(enableComboBox);

        setLoading(false);
    }

    private void processForm() {
        try {
            if (taxPayerComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, Strings.NO_TAX_PAYER_SELECTED_ERROR);
                return;
            }

            TaxPayer payer = (TaxPayer) taxPayerComboBox.getSelectedObjects()[0];

            if (taxTypeComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, Strings.NO_TAX_TYPE_SELECTED_ERROR);
                return;
            }

            TaxType taxType = (TaxType) taxTypeComboBox.getSelectedObjects()[0];
            Date created = InputParser.parseDate(datePaymentCreationField.getText());

            Date payed = null;
            if (!datePaymentPayedField.getText().isEmpty()) {
                payed = InputParser.parseDate(datePaymentPayedField.getText());
            }

            int amount = InputParser.parseInt(amountField.getText());

            SendParams params = new SendParams(payer, taxType, created, payed, amount);

            load(params, this::sendForm, this::onFormSent);
        } catch (Exception e) {
            // swallow, dialog is shown by InputParser
        }
    }

    private boolean sendForm(SendParams params) {
        DataProvider provider = getApplication().getDataProvider();

        TaxPayer payer = params.getPayer();
        TaxType taxType = params.getTaxType();
        Date created = params.getCreated();
        Date payed = params.getPayed();
        int amount = params.getAmount();

        return provider.sentPayment(payer, taxType, created, payed, amount);
    }

    private void onFormSent(boolean result) {
        String message;
        if (result) {
            message = "Platba bola spracovaná.";
        } else {
            message = "Spracovanie platby zlyhalo.";
        }

        JOptionPane.showMessageDialog(null, message);

        datePaymentCreationField.setText("");
        datePaymentPayedField.setText("");
        amountField.setText("");
        setLoading(false);
    }

    private static class SendParams {

        private final TaxPayer payer;
        private final TaxType taxType;
        private final Date created;
        private final Date payed;
        private final int amount;

        public SendParams(TaxPayer payer, TaxType taxType, Date created, Date payed, int amount) {
            this.payer = payer;
            this.taxType = taxType;
            this.created = created;
            this.payed = payed;
            this.amount = amount;
        }

        public TaxPayer getPayer() {
            return payer;
        }

        public TaxType getTaxType() {
            return taxType;
        }

        public Date getCreated() {
            return created;
        }

        public Date getPayed() {
            return payed;
        }

        public int getAmount() {
            return amount;
        }

    }

}
