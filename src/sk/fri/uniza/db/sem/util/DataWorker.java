package sk.fri.uniza.db.sem.util;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class DataWorker<P, R> extends SwingWorker<R, Void> {

    private final P params;

    private final Executer<P, R> executer;
    private final Listener<R> listener;

    public DataWorker(P params, Executer<P, R> executer, Listener<R> listener) {
        this.params = params;

        this.executer = executer;
        this.listener = listener;
    }

    @Override
    protected R doInBackground() throws Exception {
        return executer.execute(params);
    }

    @Override
    protected void done() {
        try {
            listener.onFired(get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Data loading failed", e);
        }
    }

    @FunctionalInterface
    public interface Executer<P, R> {
        R execute(P params);
    }
}
