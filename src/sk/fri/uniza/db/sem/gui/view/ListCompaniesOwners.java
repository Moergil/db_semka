package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.CompanyOwner;

import java.util.List;

public class ListCompaniesOwners extends ProviderTableView<CompanyOwner, Void> {

    private static final String[] COLUMNS = {
            "Meno",
            "Priezvisko",
            "DIC",
    };

    public ListCompaniesOwners(Application application, String title) {
        super(application, title);
    }

    @Override
    public void onShow() {
        super.onShow();
        requestTableDataLoad(null);
    }

    @Override
    protected List<CompanyOwner> loadTableDataFromProvider(DataProvider provider, Void params) {
        return provider.listCompaniesOwners();
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    protected Object[] mapRow(CompanyOwner data) {
        return toRow(data.getFirstname(), data.getSurname(), data.getDic());
    }
}
