package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WatchListRepositoryTest {
    @Test
    public void testSingletonReturnSameInstance()
    {
        WatchlistRepository instance1 = WatchlistRepository.getInstance();
        WatchlistRepository instance2 = WatchlistRepository.getInstance();

        assertSame(instance1, instance2, "WatchlistRepository sollte nur eine Instance zurück geben");
    }
    /*
    @Test
    public void testAddMovieNotificationObserver()
    {
        // Arrange
        TestObserver observer = new TestObserver();
        WatchlistRepository repo = WatchlistRepository.getInstance();
        repo.addObserver(observer);
        // Dummy-Movie erzeugen
        MovieEntity dummy = new MovieEntity(
                "123",                               // apiId
                "Testmovie",                         // title
                "description",                       // description
                2020,                                // releaseYear
                List.of(Genre.ACTION, Genre.DRAMA), // List<Genre>
                "http://example.com/poster.jpg",     // imgUrl
                120,                                 // lengthInMinutes
                8.5                                  // rating
        );
        repo.addToWatchlist(dummy.toMovie());

        // Assert
        assertTrue(observer.wasUpdated, "Observer sollte benachrigtigt werden");
        assertEquals("Film wurde erfolgreich zur Watchlist hinzugefügt.", observer.receiveMessage);

    }

     */
    // Hilfeklasse zum Beobachten
    private static class TestObserver implements Observer<Movie>{
        boolean wasUpdated = false;
        String receiveMessage = "";


        @Override
        public void update(Movie data, boolean success, String message) {
            wasUpdated = true;
            receiveMessage = message;

        }
    }

}
