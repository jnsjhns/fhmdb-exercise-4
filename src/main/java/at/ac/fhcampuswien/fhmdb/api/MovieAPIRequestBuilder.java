package at.ac.fhcampuswien.fhmdb.api;

public class MovieAPIRequestBuilder {
    private final String baseUrl;
    private String query;
    private String genre;
    private String releaseYear;
    private String ratingFrom;

    public MovieAPIRequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public MovieAPIRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    public MovieAPIRequestBuilder genre(String genre) {
        this.genre = genre;
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(String ratingFrom) {
        this.ratingFrom = ratingFrom;
        return this;
    }

    public String build() {
        StringBuilder url = new StringBuilder(baseUrl);
        boolean hasParam = false;

        if (query != null && !query.isEmpty()) {
            url.append(hasParam ? "&" : "?").append("query=").append(query);
            hasParam = true;
        }
        if (genre != null && !genre.isEmpty()) {
            url.append(hasParam ? "&" : "?").append("genre=").append(genre);
            hasParam = true;
        }
        if (releaseYear != null && !releaseYear.isEmpty()) {
            url.append(hasParam ? "&" : "?").append("releaseYear=").append(releaseYear);
            hasParam = true;
        }
        if (ratingFrom != null && !ratingFrom.isEmpty()) {
            url.append(hasParam ? "&" : "?").append("ratingFrom=").append(ratingFrom);
        }

        return url.toString();
    }
}
