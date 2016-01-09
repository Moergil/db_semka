package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public abstract class ProviderTableView<T, P> extends TableView<T, P> {

    public ProviderTableView(Application application, String title) {
        super(application, title);
    }

    protected abstract List<T> loadTableDataFromProvider(DataProvider provider, P params);

    @Override
    protected List<T> loadTableData(P params) {
        DataProvider provider = getApplication().getDataProvider();
        return loadTableDataFromProvider(provider, params);
    }

    @Override
    protected TableModel onTableDataLoaded(List<T> data) {
        DefaultTableModel tm = new DefaultTableModel(getColumns(), 0);

        for (int i = 0; i < data.size(); i++) {
            T row = data.get(i);
            tm.addRow(mapRow(row));
        }

        return tm;
    }

}
