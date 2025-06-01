package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class WatchListRepositoryTest {
    @Test
    public void testSingletonReturnSameInstance() throws DataBaseException
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


}
