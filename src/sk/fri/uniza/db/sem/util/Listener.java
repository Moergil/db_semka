package sk.fri.uniza.db.sem.util;

public interface Listener<T> {
    void onFired(T result);
}
