package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.Observer;

import com.j256.ormlite.dao.Dao;

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
    public int addToWatchlist(Movie movie) throws DataBaseException {
        try {
            long count = dao.queryBuilder().where().eq("apiId", movie.getId()).countOf();
            if (count == 0) {
                WatchlistMovieEntity entity = new WatchlistMovieEntity(movie.getId());
                int result = dao.create(entity);
                notifyObservers(movie, true, "✔ '" + movie.getTitle() + "' was added to your watchlist.");
                return result;
            } else {
                notifyObservers(movie, false, "❌ '" + movie.getTitle() + "' is already in your watchlist.");
                return 0;
            }
        } catch (Exception e) {
            notifyObservers(movie, false, "❌ Error adding '" + movie.getTitle() + "' to watchlist.");
            e.printStackTrace();
            throw new DataBaseException("Error while adding to watchlist");
        }
    }

    // Removes a movie from the watchlist by apiId
    public int removeFromWatchlist(Movie movie) throws DataBaseException {
        try {
            int result = dao.delete(dao.queryBuilder().where().eq("apiId", movie.getId()).query());
            if (result > 0) {
                notifyObservers(movie, true, "✔ '" + movie.getTitle() + "' was removed from your watchlist.");
            } else {
                notifyObservers(movie, false, "❌ '" + movie.getTitle() + "' was not in your watchlist.");
            }
            return result;
        } catch (Exception e) {
            notifyObservers(movie, false, "❌ Error removing '" + movie.getTitle() + "' from watchlist.");
            throw new DataBaseException("Error while removing from watchlist");
        }
    }
}
