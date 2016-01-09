package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;

public abstract class TableView<T, P> extends AbstractView {

    private JPanel contentComponent;
    private JScrollPane scrollPane;
    private final JTable table;

    private final String[] columns;

    public TableView(Application application, String title) {
        super(application, title);

        contentComponent = new JPanel();
        contentComponent.setLayout(new BorderLayout());

        this.table = new JTable();

        this.columns = createColumns();

        scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        contentComponent.add(BorderLayout.CENTER, scrollPane);

        setContentComponent(contentComponent);
    }

    protected void setToolbar(JToolBar toolbar) {
        toolbar.setFloatable(false);
        contentComponent.add(BorderLayout.NORTH, toolbar);
    }

    protected String[] getColumns() {
        return columns;
    }

    protected void requestTableDataLoad(P params) {
        load(params, this::loadTableData, this::onDataLoaded);
    }

    protected abstract List<T> loadTableData(P params);

    protected void onDataLoaded(final List<T> data) {
        TableModel tableModel = onTableDataLoaded(data);
        table.setModel(tableModel);

        setLoading(false);
    }

    protected abstract String[] createColumns();

    protected abstract TableModel onTableDataLoaded(List<T> data);

    protected abstract Object[] mapRow(T data);

    protected Object[] toRow(Object... objects) {
        return objects;
    }

}
