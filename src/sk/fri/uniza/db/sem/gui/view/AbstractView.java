package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.util.DataWorker;
import sk.fri.uniza.db.sem.util.Listener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractView extends JPanel implements View {

    private final Application application;

    private JPanel loadingComponent;

    private JPanel contentComponent;

    private final List<DataWorker> workers = new ArrayList<>();
    private boolean loading;

    public AbstractView(Application application) {
        this.application = application;

        this.loadingComponent = createLoadingComponent();

        setLayout(new BorderLayout());
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onHide() {
        for (DataWorker worker : workers) {
            worker.cancel(true);
        }

        workers.clear();
    }

    private JPanel createLoadingComponent() {
        JPanel loadingComponent = new JPanel();
        loadingComponent.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        c.anchor = GridBagConstraints.CENTER;

        loadingComponent.add(progressBar, c);

        return loadingComponent;
    }

    protected void setContentComponent(JPanel contentComponent) {
        this.contentComponent = contentComponent;
    }

    protected <P, D> void load(P params, DataWorker.Executer<P, D> executer, Listener<D> listener) {
        DataWorker<P, D> worker = new DataWorker<>(params, executer, listener);
        worker.execute();
        workers.add(worker);

        setLoading(true);
    }

    protected void setLoading(boolean loading) {
        int cursorType = loading ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR;
        setCursor(Cursor.getPredefinedCursor(cursorType));

        if (loading) {
            show(loadingComponent);
        } else {
            show(contentComponent);
        }
    }

    private void show(JComponent component) {
        removeAll();
        add(BorderLayout.CENTER, component);
        revalidate();
        repaint();
    }

}
