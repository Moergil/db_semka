package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxPayer;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.InputParser;

import javax.swing.*;
import java.util.Date;

public class NewTaxTypeView extends FormView {

    private final JPanel contentPanel;

    private final JTextField taxTypeNameField;

    public NewTaxTypeView(Application application) {
        super(application);

        contentPanel = new JPanel();

        taxTypeNameField = new JTextField(30);
        contentPanel.add(taxTypeNameField);

        JButton submitButton = new JButton("Pridať");
        submitButton.addActionListener((e) -> processForm());
        contentPanel.add(submitButton);

        setContentComponent(contentPanel);
    }

    @Override
    public void onShow() {
        setLoading(false);
    }

    private void processForm() {
        String taxTypeName = taxTypeNameField.getText();

        if (taxTypeName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Názov dane nieje vyplnený.");
            return;
        }

        SendParams params = new SendParams(taxTypeName);

        load(params, this::sendForm, this::onFormSent);
    }

    private boolean sendForm(SendParams params) {
        DataProvider provider = getApplication().getDataProvider();

        String taxTypeName = params.getTaxTypeName();

        return provider.addTaxType(taxTypeName);
    }

    private void onFormSent(boolean result) {
        String message;
        if (result) {
            message = "Daň bola pridaná.";
        } else {
            message = "Pridanie dane zlyhalo.";
        }

        JOptionPane.showMessageDialog(null, message);

        taxTypeNameField.setText("");
        setLoading(false);
    }

    @Override
    public String getTitle() {
        return "Nový typ dane";
    }

    private static class SendParams {

        private final String taxTypeName;

        public SendParams(String taxTypeName) {
            this.taxTypeName = taxTypeName;
        }

        public String getTaxTypeName() {
            return taxTypeName;
        }

    }
}
