package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.TaxAdvance;

import java.util.List;

public class ListTaxAdvancesView extends ProviderTableView<TaxAdvance, Void> {

    private static final String COLUMNS[] = {
            "DIC",
            "Meno",
            "ICO",
            "Celková daňová povinnosť",
            "Mesačný preddavok o rok"
    };

    public ListTaxAdvancesView(Application application, String title) {
        super(application, title);
    }

    @Override
    public void onShow() {
        super.onShow();
        requestTableDataLoad(null);
    }

    @Override
    protected List<TaxAdvance> loadTableDataFromProvider(DataProvider provider, Void params) {
        return provider.listTaxAdvances();
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    protected Object[] mapRow(TaxAdvance data) {
        return toRow(data.getDic(), data.getName(), data.getIco(), data.getTotalTaxDuty(), data.getMonthAdvancementInNextYear());
    }
}
