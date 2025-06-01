package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieApiException;
import at.ac.fhcampuswien.fhmdb.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.sort.SortContext;
import at.ac.fhcampuswien.fhmdb.sort.UnsortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.Toast;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class MovieListController implements Initializable, Observer<Movie> {
    //Make Controllers a singleton
    private static MovieListController instance;

    public MovieListController() {
        instance = this;
    }

    public static MovieListController getInstance() {
        return instance;
    }

    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    private final SortContext sortContext = new SortContext();

    private WatchlistRepository watchlistRepository;

    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) -> {
        if (clickedItem instanceof Movie movie) {
            try {
                watchlistRepository.addToWatchlist(movie);
            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("ERROR", "Could not add movie to watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            watchlistRepository = new WatchlistRepository();
            watchlistRepository.addObserver(this);
        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("ERROR", "❌ Restart the Application.");
            dialog.show();
            e.printStackTrace();
        }

        initializeState();
        initializeLayout();
    }

    @Override
    public void update(Movie movie, boolean success, String message) {
        Platform.runLater(() -> {
            Stage stage = (Stage) movieListView.getScene().getWindow();
            Toast.makeText(stage, message, 3); // Zeigt den Toast 3 Sekunden unten rechts
        });
    }

    public void initializeState() {
        List<Movie> result;
        try {
            result = MovieAPI.getAllMovies();
            writeCache(result);
        } catch (MovieApiException e){
            UserDialog dialog = new UserDialog("MovieAPI Error", "Could not load movies from api. Get movies from db cache instead");
            dialog.show();
            result = readCache();
        }

        setMovies(result);
        setMovieList(result);
        sortContext.setState(new UnsortedState());
    }

    private List<Movie> readCache() {
        try {
            MovieRepository movieRepository = new MovieRepository();
            return MovieEntity.toMovies(movieRepository.getAllMovies());
        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("DB Error", "❌ No connection to database.");
            dialog.show();
            return new ArrayList<>();
        }
    }

    private void writeCache(List<Movie> movies) {
        try {
            MovieRepository movieRepository = new MovieRepository();
            movieRepository.removeAll();
            movieRepository.addAllMovies(movies);
        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("DB Error", "❌ No connection to database.");
            dialog.show();
        }
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked));

        Object[] genres = Genre.values();
        genreComboBox.getItems().add("No filter");
        genreComboBox.getItems().addAll(genres);
        genreComboBox.setPromptText("Filter by Genre");

        releaseYearComboBox.getItems().add("No filter");
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);
        releaseYearComboBox.setPromptText("Filter by Release Year");

        ratingFromComboBox.getItems().add("No filter");
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);
        ratingFromComboBox.setPromptText("Filter by Rating");
    }

    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;
        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }
        return movies.stream().filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;
        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }
        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    public void applyAllFilters(String searchQuery, Genre genre, Integer releaseYear, Integer ratingFrom) {
        List<Movie> filteredMovies = allMovies;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null) {
            filteredMovies = filterByGenre(filteredMovies, genre);
        }

        if (releaseYear != null) {
            filteredMovies = filteredMovies.stream()
                    .filter(m -> m.getReleaseYear() == releaseYear)
                    .toList();
        }

        if (ratingFrom != null) {
            filteredMovies = filteredMovies.stream()
                    .filter(m -> m.getRating() >= ratingFrom)
                    .toList();
        }

        observableMovies.setAll(filteredMovies);
        sortContext.applyCurrentSort(observableMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();

        Genre genre = (genreComboBox.getValue() instanceof Genre) ? (Genre) genreComboBox.getValue() : null;
        Integer releaseYear = (releaseYearComboBox.getValue() instanceof Integer) ? (Integer) releaseYearComboBox.getValue() : null;
        Integer ratingFrom = (ratingFromComboBox.getValue() instanceof Integer) ? (Integer) ratingFromComboBox.getValue() : null;

        applyAllFilters(searchQuery, genre, releaseYear, ratingFrom);
    }

    public String validateComboboxValue(Object value) {
        if(value != null && !value.toString().equals("No filter")) {
            return value.toString();
        }
        return null;
    }

    public List<Movie> getMovies() {
        try{
            return MovieAPI.getAllMovies();
        }catch (MovieApiException e){
            System.out.println(e.getMessage());
            UserDialog dialog = new UserDialog("MovieApi Error", "❌ Could not load movies from api.");
            dialog.show();
            return new ArrayList<>();
        }
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortContext.sort(observableMovies);
    }
}
