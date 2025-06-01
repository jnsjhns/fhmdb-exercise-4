package at.ac.fhcampuswien.fhmdb.sort;

import javafx.collections.ObservableList;
import at.ac.fhcampuswien.fhmdb.models.Movie;

public interface SortState {
    void sort(ObservableList<Movie> movies);
    SortState nextState();
}
