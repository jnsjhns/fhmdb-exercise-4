package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.ui.Toast;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable, Observer<Movie> {

    @FXML
    public JFXListView<MovieEntity> watchlistView;

    private WatchlistRepository watchlistRepository;

    protected ObservableList<MovieEntity> observableWatchlist = FXCollections.observableArrayList();

    private final ClickEventHandler onRemoveFromWatchlistClicked = (o) -> {
        if (o instanceof MovieEntity movieEntity) {
            try {
                Movie movie = Movie.fromEntity(movieEntity);
                watchlistRepository.removeFromWatchlist(movie);
                observableWatchlist.remove(movieEntity);
            } catch (DataBaseException e) {
                Stage stage = (Stage) watchlistView.getScene().getWindow();
                Toast.makeText(stage, "Could not remove movie from watchlist", 3);
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<WatchlistMovieEntity> watchlist = new ArrayList<>();
        try {
            watchlistRepository = new WatchlistRepository();
            watchlistRepository.addObserver(this);

            watchlist = watchlistRepository.getWatchlist();

            MovieRepository movieRepository = new MovieRepository();
            List<MovieEntity> movies = new ArrayList<>();

            for (WatchlistMovieEntity movie : watchlist) {
                MovieEntity movieEntity = movieRepository.getMovie(movie.getApiId());
                if (movieEntity != null) {
                    movies.add(movieEntity);
                }
            }

            observableWatchlist.setAll(movies);
            watchlistView.setItems(observableWatchlist);
            watchlistView.setCellFactory(movieListView -> new WatchlistCell(onRemoveFromWatchlistClicked));

        } catch (DataBaseException e) {
            Stage stage = (Stage) watchlistView.getScene().getWindow();
            Toast.makeText(stage, "Could not read movies from DB", 3);
            e.printStackTrace();
        }

        if (watchlist.size() == 0) {
            watchlistView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }

        System.out.println("WatchlistController initialized");
    }

    @Override
    public void update(Movie movie, boolean success, String message) {
        Platform.runLater(() -> {
            // Aktualisiere die Watchlist-Ansicht
            try {
                List<WatchlistMovieEntity> watchlist = watchlistRepository.getWatchlist();
                MovieRepository movieRepository = new MovieRepository();
                List<MovieEntity> movies = new ArrayList<>();
                for (WatchlistMovieEntity entity : watchlist) {
                    MovieEntity movieEntity = movieRepository.getMovie(entity.getApiId());
                    if (movieEntity != null) {
                        movies.add(movieEntity);
                    }
                }
                observableWatchlist.setAll(movies);
            } catch (DataBaseException e) {
                // Fehlerbehandlung optional
            }

            // Zeige Toast-Benachrichtigung unten rechts
            Stage stage = (Stage) watchlistView.getScene().getWindow();
            Toast.makeText(stage, message, 3);
        });
    }
}
