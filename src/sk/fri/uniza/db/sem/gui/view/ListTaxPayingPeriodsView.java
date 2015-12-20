package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.Config;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxPayingPeriod;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ListTaxPayingPeriodsView extends ProviderTableView<TaxPayingPeriod, ListTaxPayingPeriodsView.DateFromTo> {

    private static final String COLUMNS[] = {
            "Úrad",
            "Mesiac",
            "Rok",
            "Počet platcov"
    };

    private final JTextField dateFromField;
    private final JTextField dateToField;

    public ListTaxPayingPeriodsView(Application application) {
        super(application);

        int dateInputColumns = Config.GUI_DATE_INPUT_COLUMNS;
        dateFromField = new JTextField(dateInputColumns);
        dateToField = new JTextField(dateInputColumns);

        JToolBar toolbar = createToolbar();
        setToolbar(toolbar);
    }

    @Override
    public void onShow() {
        super.onShow();

        setLoading(false);
    }

    @Override
    protected List<TaxPayingPeriod> loadTableDataFromProvider(DataProvider provider, DateFromTo params) {
        Date from = params.getFrom();
        Date to = params.getTo();
        return provider.listTaxPayingPeriods(from, to);
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    public String getTitle() {
        return "Obdobia platcov";
    }

    public JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();

        JLabel dateFromLabel = new JLabel(Strings.DATE_FROM);
        toolbar.add(dateFromLabel);

        toolbar.add(dateFromField);

        JLabel dateToLabel = new JLabel(Strings.DATO_TO);
        toolbar.add(dateToLabel);

        toolbar.add(dateToField);

        JButton button = new JButton(Strings.SEARCH);
        button.addActionListener((e) -> {
            DateFromTo params = extractDates();

            if (params != null) {
                requestTableDataLoad(params);
            }
        });
        toolbar.add(button);

        return toolbar;
    }

    private DateFromTo extractDates() {
        try {
            String dateFromText = dateFromField.getText();
            Date from = InputParser.parseDate(dateFromText);

            String dateToText = dateToField.getText();
            Date to = InputParser.parseDate(dateToText);

            return new DateFromTo(from, to);
        } catch (ParseException e) {
            return null;
        }
    }

    static class DateFromTo {
        private final Date from;
        private final Date to;

        public DateFromTo(Date from, Date to) {
            this.from = from;
            this.to = to;
        }

        public Date getFrom() {
            return from;
        }

        public Date getTo() {
            return to;
        }
    }
}
