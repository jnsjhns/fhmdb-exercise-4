package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WatchlistRepositoryTest {

    // Test-Observer-Implementation
    static class TestObserver implements Observer<Movie> {
        boolean notified = false;
        Movie lastMovie = null;
        boolean lastSuccess = false;
        String lastMessage = null;

        @Override
        public void update(Movie data, boolean success, String message) {
            notified = true;
            lastMovie = data;
            lastSuccess = success;
            lastMessage = message;
        }

        void reset() {
            notified = false;
            lastMovie = null;
            lastSuccess = false;
            lastMessage = null;
        }
    }

    WatchlistRepository repo;
    TestObserver observer;
    Movie movie;

    @BeforeEach
    void setUp() throws Exception {
        repo = new WatchlistRepository();
        observer = new TestObserver();
        repo.addObserver(observer);
        movie = new Movie("test title", "test descr.", null);
    }

    // --- Observer Pattern Tests ---

    @Test
    void givenObserverRegistered_whenAddToWatchlist_thenObserverIsNotifiedWithSuccess() throws Exception {
        // GIVEN (setUp)

        // WHEN
        repo.addToWatchlist(movie);

        // THEN
        assertTrue(observer.notified, "Observer should be notified");
        assertEquals(movie, observer.lastMovie, "Observer should get correct movie");
        assertTrue(observer.lastSuccess, "Observer should be notified with success");
        assertNotNull(observer.lastMessage, "Observer should get a message");
        assertTrue(observer.lastMessage.contains("added"), "Message should indicate addition");
    }

    @Test
    void givenMovieAlreadyInWatchlist_whenAddToWatchlist_thenObserverIsNotifiedWithFailure() throws Exception {
        // GIVEN
        repo.addToWatchlist(movie);
        observer.reset();

        // WHEN
        repo.addToWatchlist(movie);

        // THEN
        assertTrue(observer.notified, "Observer should be notified");
        assertEquals(movie, observer.lastMovie, "Observer should get correct movie");
        assertFalse(observer.lastSuccess, "Observer should be notified with failure");
        assertNotNull(observer.lastMessage, "Observer should get a message");
        assertTrue(observer.lastMessage.contains("already"), "Message should indicate already in watchlist");
    }

    @Test
    void givenObserverRemoved_whenAddToWatchlist_thenObserverIsNotNotified() throws Exception {
        // GIVEN
        repo.removeObserver(observer);

        // WHEN
        repo.addToWatchlist(movie);

        // THEN
        assertFalse(observer.notified, "Observer should not be notified after removal");
    }

    @Test
    void givenMultipleObservers_whenAddToWatchlist_thenAllObserversAreNotified() throws Exception {
        // GIVEN
        TestObserver observer2 = new TestObserver();
        repo.addObserver(observer2);

        // WHEN
        repo.addToWatchlist(movie);

        // THEN
        assertTrue(observer.notified, "First observer should be notified");
        assertTrue(observer2.notified, "Second observer should be notified");
    }



    @Test
    void givenMovieInWatchlist_whenRemoveFromWatchlist_thenMovieIsRemovedAndObserverNotified() throws Exception {
        // GIVEN
        repo.addToWatchlist(movie);
        observer.reset();

        // WHEN
        repo.removeFromWatchlist(movie);

        // THEN
        assertTrue(observer.notified, "Observer should be notified");
        assertEquals(movie, observer.lastMovie, "Observer should get correct movie");
        assertTrue(observer.lastSuccess, "Observer should be notified with success");
        assertNotNull(observer.lastMessage, "Observer should get a message");
        assertTrue(observer.lastMessage.contains("removed"), "Message should indicate removal");
    }

    @Test
    void givenMovieNotInWatchlist_whenRemoveFromWatchlist_thenObserverIsNotifiedWithFailure() throws Exception {
        // GIVEN (movie not in watchlist)

        // WHEN
        repo.removeFromWatchlist(movie);

        // THEN
        assertTrue(observer.notified, "Observer should be notified");
        assertEquals(movie, observer.lastMovie, "Observer should get correct movie");
        assertFalse(observer.lastSuccess, "Observer should be notified with failure");
        assertNotNull(observer.lastMessage, "Observer should get a message");
        assertTrue(observer.lastMessage.contains("not in your watchlist") || observer.lastMessage.contains("not in the watchlist"), "Message should indicate not in watchlist");
    }
}
