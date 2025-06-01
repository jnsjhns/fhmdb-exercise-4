package at.ac.fhcampuswien.fhmdb.sort;

import javafx.collections.ObservableList;
import at.ac.fhcampuswien.fhmdb.models.Movie;

public class SortContext {
    private SortState currentState;

    public SortContext() {
        this.currentState = new UnsortedState();
    }

    public void sort(ObservableList<Movie> movies) {
        currentState = currentState.nextState();
        currentState.sort(movies);
    }

    public void setState(SortState state) {
        this.currentState = state;
    }

    public void applyCurrentSort(ObservableList<Movie> movies) {
        currentState.sort(movies);
    }
}
