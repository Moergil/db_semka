package sk.fri.uniza.db.sem;

import sk.fri.uniza.db.sem.db.DataProvider;
import sk.fri.uniza.db.sem.db.OracleSqlDataProvider;
import sk.fri.uniza.db.sem.gui.MainWindow;
import sk.fri.uniza.db.sem.gui.SplashLoading;
import sk.fri.uniza.db.sem.util.Listener;

import javax.swing.*;

public class Application implements Runnable {

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new Application().run());
    }

    private DataProvider dataProvider;

    private SplashLoading splashLoading;

    public Application() {
        this.splashLoading = new SplashLoading();
    }

    @Override
    public void run() {
        initializeDataProvider((dp) -> dataProviderInitialized(dp));
    }

    private void initializeDataProvider(Listener<DataProvider> listener) {
        splashLoading.show();

        new SwingWorker<DataProvider, Void>() {
            @Override
            protected DataProvider doInBackground() throws Exception {
                DataProvider dataProvider = new OracleSqlDataProvider();
                //DataProvider dataProvider = new FakeDataProvider();

                dataProvider.open();

                return dataProvider;
            }

            @Override
            protected void done() {
                splashLoading.hide();

                try {
                    listener.onFired(get());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Initialization failed: " + e.getMessage());
                }
            }


        }.execute();
    }

    private void dataProviderInitialized(DataProvider dataProvider) {
        this.dataProvider = dataProvider;

        new MainWindow(Application.this).show();
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }
}
