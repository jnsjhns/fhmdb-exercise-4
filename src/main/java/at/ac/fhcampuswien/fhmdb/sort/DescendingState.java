package at.ac.fhcampuswien.fhmdb.sort;

import javafx.collections.ObservableList;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;

public class DescendingState implements SortState {
    @Override
    public void sort(ObservableList<Movie> movies) {
        movies.sort(Comparator.comparing(Movie::getTitle).reversed());
    }

    @Override
    public SortState nextState() {
        return new AscendingState();
    }
}
