package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.Company;

import java.util.List;

public class ListCompanies extends ProviderTableView<Company, Void> {

    private static final String[] COLUMNS = {
            "Meno",
            "DIC"
    };

    public ListCompanies(Application application) {
        super(application);
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
    public String getTitle() {
        return "Spoločnosti";
    }
}
