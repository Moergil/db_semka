package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.Config;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.IncomeComposition;
import sk.fri.uniza.db.sem.db.model.SubjectType;
import sk.fri.uniza.db.sem.db.model.TaxType;
import sk.fri.uniza.db.sem.util.DataWorker;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class ListIncomeCompositionsView extends ProviderTableView<IncomeComposition, ListIncomeCompositionsView.IncomeCompositionParams> {

    private static final String COLUMNS[] = {
            "Rok",
            "Mesiac",
            "Mesto",
            "Príjem"
    };

    private static final SubjectType PHYSICAL_PERSON = new SubjectType(1, "Fyzická osoba");
    private static final SubjectType JURIDICAL_PERSON = new SubjectType(0, "Právnická osoba");

    private final JTextField dateFromField;
    private final JTextField dateToField;
    private JComboBox<TaxType> taxTypesComboBox;
    private JComboBox<SubjectType> subjectTypesComboBox;

    private DataWorker<Void, TaxType[]> taxTypesLoader;

    public ListIncomeCompositionsView(Application application) {
        super(application);

        int dateInputColumns = Config.DATE_MAX_LENGTH;
        dateFromField = new JTextField(dateInputColumns);
        dateToField = new JTextField(dateInputColumns);

        taxTypesComboBox = new JComboBox<>();

        SubjectType[] subjectTypes = {PHYSICAL_PERSON, JURIDICAL_PERSON};
        subjectTypesComboBox = new JComboBox<>(subjectTypes);

        JToolBar toolbar = createToolbar();
        setToolbar(toolbar);
    }

    public JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        JLabel dateFromLabel = new JLabel(Strings.DATE_FROM);

        c.gridx = 0;
        c.gridy = 0;
        toolbar.add(dateFromLabel, c);

        c.gridx++;
        toolbar.add(dateFromField, c);

        c.gridx++;
        JLabel dateToLabel = new JLabel(Strings.DATO_TO);
        toolbar.add(dateToLabel, c);

        c.gridx++;
        toolbar.add(dateToField, c);

        c.gridx = 0;
        c.gridy++;
        JLabel taxTypeLabel = new JLabel(Strings.TAX_TYPE);
        toolbar.add(taxTypeLabel, c);

        c.gridx++;
        toolbar.add(taxTypesComboBox, c);

        c.gridx++;
        JLabel subjectTypeLable = new JLabel(Strings.SUBJECT_TYPE);
        toolbar.add(subjectTypeLable, c);

        c.gridx++;
        toolbar.add(subjectTypesComboBox, c);

        c.gridx++;
        c.gridy--;
        c.gridheight = 2;
        JButton button = new JButton(Strings.SEARCH);
        button.addActionListener((e) -> {
            IncomeCompositionParams params = createParams();

            if (params != null) {
                requestTableDataLoad(params);
            }
        });
        toolbar.add(button, c);

        return toolbar;
    }

    private IncomeCompositionParams createParams() {
        try {
            String dateFromText = dateFromField.getText();
            Date dateFrom = InputParser.parseDate(dateFromText);

            String dateToText = dateToField.getText();
            Date dateTo = InputParser.parseDate(dateToText);

            TaxType taxType = (TaxType) taxTypesComboBox.getSelectedObjects()[0];
            SubjectType subjectType = (SubjectType) subjectTypesComboBox.getSelectedObjects()[0];

            return new IncomeCompositionParams(dateFrom, dateTo, taxType.getType(), subjectType.getType());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onShow() {
        super.onShow();

        taxTypesLoader = new DataWorker<>(null, this::loadTaxTypes, this::onTaxTypesLoaded);
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
    protected List<IncomeComposition> loadTableDataFromProvider(DataProvider provider, IncomeCompositionParams params) {
        return provider.listIncomeCompositions(params.getFrom(), params.getTo(), params.getTaxType(), params.getPersonType());
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    public String getTitle() {
        return "Zloženie príjmov";
    }

    static class IncomeCompositionParams {
        private final Date from;
        private final Date to;
        private int taxType;
        private int personType;

        public IncomeCompositionParams(Date from, Date to, int taxType, int personType) {
            this.from = from;
            this.to = to;
            this.taxType = taxType;
            this.personType = personType;
        }

        public Date getFrom() {
            return from;
        }

        public Date getTo() {
            return to;
        }

        public int getTaxType() {
            return taxType;
        }

        public int getPersonType() {
            return personType;
        }
    }
}
