package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.Company;

import java.util.List;

public class ListCompaniesView extends ProviderTableView<Company, Void> {

    private static final String[] COLUMNS = {
            "Meno",
            "DIC"
    };

    public ListCompaniesView(Application application, String title) {
        super(application, title);
    }

    @Override
    protected List<Company> loadTableDataFromProvider(DataProvider provider, Void params) {
        return provider.listCompanies();
    }

    @Override
    public void onShow() {
        super.onShow();
        requestTableDataLoad(null);
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    protected Object[] mapRow(Company data) {
        return toRow(data.getName(), data.getDic());
    }
}
