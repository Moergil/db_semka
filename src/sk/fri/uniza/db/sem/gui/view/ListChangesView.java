package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.JuridicalPersonName;
import sk.fri.uniza.db.sem.util.InputParser;
import sk.fri.uniza.db.sem.util.Strings;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class ListChangesView extends ProviderTableView<JuridicalPersonName, ListChangesView.TimeRangeParams> {

    private static final String COLUMNS[] = {
            "Názov"
    };

    private final JTextField dateFromField;
    private final JTextField dateToField;

    public ListChangesView(Application application, String title) {
        super(application, title);

        dateFromField = new JTextField();
        dateToField = new JTextField();

        JToolBar toolbar = createToolbar();
        setToolbar(toolbar);
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();

        JLabel dateFromLabel = new JLabel(Strings.DATE_FROM);

        toolbar.add(dateFromLabel);

        toolbar.add(dateFromField);

        JLabel dateToLabel = new JLabel(Strings.DATO_TO);
        toolbar.add(dateToLabel);

        toolbar.add(dateToField);

        JButton button = new JButton(Strings.SEARCH);
        button.addActionListener((e) -> {
            TimeRangeParams params = createParams();

            if (params != null) {
                requestTableDataLoad(params);
            }
        });
        toolbar.add(button);

        return toolbar;
    }

    private TimeRangeParams createParams() {
        try {
            String dateFromText = dateFromField.getText();
            Date dateFrom = InputParser.parseDate(dateFromText);

            String dateToText = dateToField.getText();
            Date dateTo = InputParser.parseDate(dateToText);

            return new TimeRangeParams(dateFrom, dateTo);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onShow() {
        super.onShow();

        setLoading(false);
    }

    @Override
    protected List<JuridicalPersonName> loadTableDataFromProvider(DataProvider provider, TimeRangeParams params) {
        Date from = params.getFrom();
        Date to = params.getTo();

        return provider.listChanges(from, to);
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    protected Object[] mapRow(JuridicalPersonName data) {
        return toRow(data.getName());
    }

    protected static class TimeRangeParams {

        private final Date from;
        private final Date to;

        public TimeRangeParams(Date from, Date to) {
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
