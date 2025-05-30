package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPIRequestBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MovieAPIRequestBuilderTest {

    @Test
    void buildUrl_withAllParameters_returnsCorrectUrl() {
        String base = "https://prog2.fh-campuswien.ac.at/movies";
        String expected = "https://prog2.fh-campuswien.ac.at/movies?query=test&genre=ACTION&releaseYear=2020&ratingFrom=8";

        String actual = new MovieAPIRequestBuilder(base)
                .query("test")
                .genre("ACTION")
                .releaseYear("2020")
                .ratingFrom("8")
                .build();

        assertEquals(expected, actual);
    }

    @Test
    void buildUrl_withNoParameters_returnsBaseUrl() {
        String base = "https://prog2.fh-campuswien.ac.at/movies";

        String actual = new MovieAPIRequestBuilder(base).build();

        assertEquals(base, actual);
    }

    @Test
    void buildUrl_withOnlyQuery_returnsCorrectUrl() {
        String base = "https://prog2.fh-campuswien.ac.at/movies";
        String expected = "https://prog2.fh-campuswien.ac.at/movies?query=test";

        String actual = new MovieAPIRequestBuilder(base)
                .query("test")
                .build();

        assertEquals(expected, actual);
    }
    @Test
    void buildUrl_withEmptyParameters_returnsBaseUrl() {
        String base = "https://prog2.fh-campuswien.ac.at/movies";

        String actual = new MovieAPIRequestBuilder(base)
                .query("")
                .genre("")
                .releaseYear("")
                .ratingFrom("")
                .build();

        assertEquals(base, actual);
    }
    @Test
    void buildUrl_withSomeParameters_returnsCorrectPartialUrl() {
        String base = "https://prog2.fh-campuswien.ac.at/movies";
        String expected = "https://prog2.fh-campuswien.ac.at/movies?genre=ACTION&ratingFrom=7";

        String actual = new MovieAPIRequestBuilder(base)
                .genre("ACTION")
                .ratingFrom("7")
                .build();

        assertEquals(expected, actual);
    }
}
