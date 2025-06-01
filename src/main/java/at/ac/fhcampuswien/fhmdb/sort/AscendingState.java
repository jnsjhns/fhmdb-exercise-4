package at.ac.fhcampuswien.fhmdb.sort;

import javafx.collections.ObservableList;
import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;

public class AscendingState implements SortState {
    @Override
    public void sort(ObservableList<Movie> movies) {
        movies.sort(Comparator.comparing(Movie::getTitle));
    }

    @Override
    public SortState nextState() {
        return new DescendingState();
    }
}
