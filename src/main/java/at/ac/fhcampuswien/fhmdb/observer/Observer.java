package at.ac.fhcampuswien.fhmdb.observer;

// A generic listener which receives updates from an Observable.
// @param <T> = type of data sent by the Observable
public interface Observer<T> {


    // Called whenever the observed Observable issues an update.
    // @param data    the payload (e.g. the Movie)
    // @param success true if the operation succeeded, false otherwise
    // @param message human-readable feedback text
    void update(T data, boolean success, String message);
}
