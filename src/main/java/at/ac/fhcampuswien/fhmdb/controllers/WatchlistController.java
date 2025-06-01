package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.ui.Toast;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
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
    //Make Controllers a singleton
    private static WatchlistController instance;

    public WatchlistController() {
        instance = this;
    }

    public static WatchlistController getInstance() {
        return instance;
    }

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
                new UserDialog("DB ERROR", "❌ Please restart the app.");
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<WatchlistMovieEntity> watchlist = new ArrayList<>();
        try {
            watchlistRepository = WatchlistRepository.getInstance();
            watchlistRepository.addObserver(this);

            watchlist = watchlistRepository.getWatchlist();

            MovieRepository movieRepository = MovieRepository.getInstance();
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
            new UserDialog("DB ERROR", "❌ Please restart the app.");
            e.printStackTrace();
        }

        if (watchlist.size() == 0) {
            watchlistView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }

        System.out.println("WatchlistController initialized");
    }

    // Method from Interface Observer
    @Override
    public void update(Movie movie, boolean success, String message) {
        // Ensures that all UI updates inside this block are executed on the JavaFX Application Thread
        // Platform.runLater() schedules the code asynchronously for the UI thread
        Platform.runLater(() -> {

            // Update the observable watchlist in the UI with the latest data from the database
            try {
                List<WatchlistMovieEntity> watchlist = watchlistRepository.getWatchlist();
                MovieRepository movieRepository = MovieRepository.getInstance();
                List<MovieEntity> movies = new ArrayList<>();
                for (WatchlistMovieEntity entity : watchlist) {
                    MovieEntity movieEntity = movieRepository.getMovie(entity.getApiId());
                    if (movieEntity != null) {
                        movies.add(movieEntity);
                    }
                }
                observableWatchlist.setAll(movies);

            } catch (DataBaseException e) {
                new UserDialog("Database Error", "❌ Failed to update the watchlist. Please try again or restart the app.").show();
                e.printStackTrace();
            }

            // Always show the notification toast (success or failure) to the user
            Stage stage = (Stage) watchlistView.getScene().getWindow();
            Toast.makeText(stage, message, 3);
        });
    }


}
