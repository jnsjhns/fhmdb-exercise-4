package at.ac.fhcampuswien.fhmdb.sort;

import javafx.collections.ObservableList;
import at.ac.fhcampuswien.fhmdb.models.Movie;

public class UnsortedState implements SortState {
    @Override
    public void sort(ObservableList<Movie> movies) {}

    @Override
    public SortState nextState() {
        return new AscendingState();
    }
}
