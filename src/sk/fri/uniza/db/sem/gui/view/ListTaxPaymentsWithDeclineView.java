package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxPayment;

import java.util.List;

public class ListTaxPaymentsWithDeclineView extends ProviderTableView<TaxPayment, Void> {

    private static final String COLUMNS[] = {
            "Platca",
            "Rok",
            "Daň"
    };

    public ListTaxPaymentsWithDeclineView(Application application, String title) {
        super(application, title);
    }

    @Override
    public void onShow() {
        super.onShow();
        requestTableDataLoad(null);
    }

    @Override
    protected List<TaxPayment> loadTableDataFromProvider(DataProvider provider, Void params) {
        return provider.listTaxPaymentsWithDecline();
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    protected Object[] mapRow(TaxPayment data) {
        return toRow(data.getTaxPayerName(), data.getYear(), data.getTax());
    }
}
