package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.Config;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class TaxSettingView extends FormView {

    private final JPanel contentComponent;

    private final JComboBox<TaxType> taxTypeComboBox;
    private final JTextField taxPercentField;
    private final JTextField dateValidFromField;
    private final JTextField dateValidToField;

    public TaxSettingView(Application application, String title) {
        super(application, title);

        contentComponent = new JPanel();
        contentComponent.setLayout(new GridBagLayout());

        taxTypeComboBox = new JComboBox<>();

        taxPercentField = new JTextField(3);

        dateValidFromField = new JTextField(Config.DATE_MAX_LENGTH);
        dateValidToField = new JTextField(Config.DATE_MAX_LENGTH);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;

        JLabel taxType = new JLabel(Strings.TAX_TYPE);
        contentComponent.add(taxType, c);

        c.gridx++;

        contentComponent.add(taxTypeComboBox, c);

        c.gridx++;

        contentComponent.add(taxPercentField, c);

        c.gridx++;

        JLabel percentLabel = new JLabel("%");
        contentComponent.add(percentLabel, c);

        c.gridx = 0;
        c.gridy++;

        JLabel dateFromLabel = new JLabel(Strings.DATE_FROM);
        contentComponent.add(dateFromLabel, c);

        c.gridx++;

        contentComponent.add(dateValidFromField, c);

        c.gridx++;

        JLabel dateToLabel = new JLabel(Strings.DATO_TO);
        contentComponent.add(dateToLabel, c);

        c.gridx++;

        contentComponent.add(dateValidToField, c);

        c.gridx = 2;
        c.gridy++;
        c.gridwidth = 2;

        JButton submitButton = new JButton(Strings.MODIFY);
        submitButton.addActionListener((e) -> processForm());
        contentComponent.add(submitButton, c);

        setContentComponent(contentComponent);
    }

    @Override
    public void onShow() {
        load(null, this::loadTaxTypes, this::onTaxTypesLoaded);
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

        setLoading(false);
    }

    private void processForm() {
        try {
            if (taxTypeComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(null, Strings.NO_TAX_TYPE_SELECTED_ERROR);
                return;
            }

            TaxType taxType = (TaxType) taxTypeComboBox.getSelectedObjects()[0];

            String percentValue = taxPercentField.getText();
            int percent = InputParser.parseInt(percentValue);

            if (percent < 0 || percent > 100) {
                JOptionPane.showMessageDialog(null, Strings.PERCENT_RANGE_ERROR);
                return;
            }

            String dateFromString = dateValidFromField.getText();
            Date dateFrom = InputParser.parseDate(dateFromString);

            String dateToString = dateValidToField.getText();
            Date dateTo = InputParser.parseDate(dateToString);

            SendParams sendParams = new SendParams(taxType, percent, dateFrom, dateTo);

            load(sendParams, this::sendForm, this::onFormSent);
        } catch (Exception e) {
            // swallow, dialog is shown by InputParser
        }
    }

    private boolean sendForm(SendParams params) {
        DataProvider provider = getApplication().getDataProvider();

        TaxType taxType = params.getTaxType();
        int percent = params.getPercent();
        Date dateFrom = params.getValidFromDate();
        Date dateTo = params.getValidToDate();

        return provider.setTax(taxType, percent, dateFrom, dateTo);
    }

    private void onFormSent(boolean result) {
        String message;
        if (result) {
            message = "Daň bola upravená.";
        } else {
            message = "Upravenie dane zlyhalo.";
        }

        JOptionPane.showMessageDialog(null, message);

        taxPercentField.setText("");
        dateValidFromField.setText("");
        dateValidToField.setText("");

        setLoading(false);
    }

    private static class SendParams {

        private TaxType taxType;
        private int percent;
        private Date validFromDate;
        private Date validToDate;

        public SendParams(TaxType taxType, int percent, Date validFromDate, Date validToDate) {
            this.taxType = taxType;
            this.percent = percent;
            this.validFromDate = validFromDate;
            this.validToDate = validToDate;
        }

        public TaxType getTaxType() {
            return taxType;
        }

        public int getPercent() {
            return percent;
        }

        public Date getValidFromDate() {
            return validFromDate;
        }

        public Date getValidToDate() {
            return validToDate;
        }

    }
}
