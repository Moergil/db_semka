package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.model.Penalty;

import java.util.List;

public class PenaltiesView extends ProviderTableView<Penalty, Void> {

    private static final String[] COLUMNS = {
            "Meno",
            "Dátum vzniku",
            "Stav platby",
            "Suma",
            "Percento",
            "Penále"
    };

    public PenaltiesView(Application application, String title) {
        super(application, title);
    }

    @Override
    public void onShow() {
        super.onShow();
        requestTableDataLoad(null);
    }

    @Override
    protected List<Penalty> loadTableDataFromProvider(DataProvider provider, Void params) {
        return provider.listPenalties();
    }

    @Override
    protected String[] createColumns() {
        return COLUMNS;
    }

    @Override
    protected Object[] mapRow(Penalty data) {
        return toRow(data.getPayerName(), data.getDateCreated(), data.getPaymentState(), data.getAmount(), data.getPercent(), data.getPenalty());
    }
}
