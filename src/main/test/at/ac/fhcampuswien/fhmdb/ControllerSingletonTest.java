import at.ac.fhcampuswien.fhmdb.controllers.MainController;
import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;
import at.ac.fhcampuswien.fhmdb.controllers.WatchlistController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerSingletonTest {

    @Test
    public void testMainControllerIsSingleton() {
        MainController firstInstance = new MainController();
        MainController secondInstance = MainController.getInstance();

        assertNotNull(secondInstance);
        assertSame(firstInstance, secondInstance);
    }

    @Test
    public void testMovieListControllerIsSingleton() {
        MovieListController firstInstance = new MovieListController();
        MovieListController secondInstance = MovieListController.getInstance();

        assertNotNull(secondInstance);
        assertSame(firstInstance, secondInstance);
    }

    @Test
    public void testWatchlistControllerIsSingleton() {
        WatchlistController firstInstance = new WatchlistController();
        WatchlistController secondInstance = WatchlistController.getInstance();

        assertNotNull(secondInstance);
        assertSame(firstInstance, secondInstance);
    }
}
import at.ac.fhcampuswien.fhmdb.controllers.MainController;
import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;
import at.ac.fhcampuswien.fhmdb.controllers.WatchlistController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerSingletonTest {

    @Test
    public void testMainControllerIsSingleton() {
        MainController firstInstance = new MainController();
        MainController secondInstance = MainController.getInstance();

        assertNotNull(secondInstance);
        assertSame(firstInstance, secondInstance);
    }

    @Test
    public void testMovieListControllerIsSingleton() {
        MovieListController firstInstance = new MovieListController();
        MovieListController secondInstance = MovieListController.getInstance();

        assertNotNull(secondInstance);
        assertSame(firstInstance, secondInstance);
    }

    @Test
    public void testWatchlistControllerIsSingleton() {
        WatchlistController firstInstance = new WatchlistController();
        WatchlistController secondInstance = WatchlistController.getInstance();

        assertNotNull(secondInstance);
        assertSame(firstInstance, secondInstance);
    }
}
