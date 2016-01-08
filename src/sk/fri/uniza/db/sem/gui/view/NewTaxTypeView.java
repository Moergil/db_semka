package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;

public class NewTaxTypeView extends FormView {

    private final JPanel contentComponent;

    private final JTextField taxTypeNameField;

    public NewTaxTypeView(Application application) {
        super(application);

        contentComponent = new JPanel();

        JLabel taxTypeNameLabel = new JLabel(Strings.TAX_TYPE);
        contentComponent.add(taxTypeNameLabel);

        taxTypeNameField = new JTextField(30);
        contentComponent.add(taxTypeNameField);

        JButton submitButton = new JButton(Strings.ADD);
        submitButton.addActionListener((e) -> processForm());
        contentComponent.add(submitButton);

        setContentComponent(contentComponent);
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
