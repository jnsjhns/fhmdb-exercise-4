package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.Observer;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable<Movie> {

    private final List<Observer<Movie>> observers = new ArrayList<>();

    Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository() throws DataBaseException {
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    @Override
    public void addObserver(Observer<Movie> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<Movie> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Movie data, boolean success, String message) {
        for (Observer<Movie> o : observers) {
            o.update(data, success, message);
        }
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while reading watchlist");
        }
    }

    // Adds a movie to the watchlist if it does not already exist
    // Notifies all registered observers about the result
    public void addToWatchlist(Movie movie) throws DataBaseException {
        try {
            // Check if the movie is already in the watchlist (by apiId)
            long count = dao.queryBuilder().where().eq("apiId", movie.getId()).countOf();
            if (count == 0) {
                // Movie was not yet in watchlist -> add it + notify observers
                WatchlistMovieEntity entity = new WatchlistMovieEntity(movie.getId());
                dao.create(entity);
                notifyObservers(movie, true, "✔ '" + movie.getTitle() + "' was added to your watchlist.");
            } else {
                // Movie was already in watchlist -> notify observers
                notifyObservers(movie, false, "❌ '" + movie.getTitle() + "' is already in your watchlist.");
            }

        } catch (SQLException e) {
            // Handle SQL/database errors and notify observers
            notifyObservers(movie, false, "❌ Database error: " + e.getMessage());
            e.printStackTrace();
            throw new DataBaseException("Database error while adding " + movie + " to watchlist");
        }
    }

    // Removes a movie from the watchlist by apiId
    // Notifies all registered observers about the result
    public void removeFromWatchlist(Movie movie) throws DataBaseException {
        try {
            // Attempt to delete the movie from the watchlist (by apiId)
            int result = dao.delete(dao.queryBuilder().where().eq("apiId", movie.getId()).query());
            if (result > 0) {
                // Movie was successfully removed; notify observers
                notifyObservers(movie, true, "✔ '" + movie.getTitle() + "' was removed from your watchlist.");
            } else {
                // Movie was not found in the watchlist; notify observers
                notifyObservers(movie, false, "❌ '" + movie.getTitle() + "' was not in your watchlist.");
            }

        } catch (SQLException e) {
            // Handle SQL/database errors and notify observers
            notifyObservers(movie, false, "❌ Error removing '" + movie.getTitle() + "' from watchlist.");
            e.printStackTrace();
            throw new DataBaseException("Error while removing " + movie + " from watchlist");
        }
    }

}
