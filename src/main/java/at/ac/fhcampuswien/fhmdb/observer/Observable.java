package at.ac.fhcampuswien.fhmdb.observer;

// A generic subject which can be observed.
// @param <T> = the type of data passed to observers
public interface Observable<T> {

    // Register a new observer.
    void addObserver(Observer<T> observer);

    // Unregister an existing observer.
    void removeObserver(Observer<T> observer);

    // Notify all registered observers with the given data, success flag and message.
    // @param data    the payload (e.g. the Movie)
    // @param success true if the operation succeeded, false otherwise
    // @param message human-readable feedback text
    void notifyObservers(T data, boolean success, String message);
}
