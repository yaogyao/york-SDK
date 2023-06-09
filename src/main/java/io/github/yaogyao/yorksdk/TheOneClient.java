package io.github.yaogyao.yorksdk;

import io.github.yaogyao.yorksdk.model.Movie;
import io.github.yaogyao.yorksdk.model.Quote;
import io.github.yaogyao.yorksdk.model.Response;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.util.Map;

/**
 * A client class to access The One API.
 * It uses Feign library to handle lower level http request/response.
 * @see Options
 * @see ExampleUsage
 */
public class TheOneClient {

    @Headers("Authorization: Bearer {token}")
    private interface TheOne {
        @RequestLine("GET /movie?{filterPattern}")
        Response<Movie> movies(@Param("token") String token, @QueryMap Map<String, Object> queryMap, @Param(value = "filterPattern") String filter);

        @RequestLine("GET /movie/{id}")
        Response<Movie> movie(@Param("token") String token, @Param("id") String movieId);

        @RequestLine("GET /movie/{id}/quote?{filterPattern}")
        Response<Quote> quotesByMovie(@Param("token") String token, @Param("id") String movieId, @QueryMap Map<String, Object> queryMap, @Param(value = "filterPattern") String filter);

        @RequestLine("GET /quote?{filterPattern}")
        Response<Quote> quotes(@Param("token") String token, @QueryMap Map<String, Object> queryMap, @Param(value = "filterPattern") String filter);

        @RequestLine("GET /quote/{id}?{filterPattern}")
        Response<Quote> quote(@Param("token") String token, @Param("id") String quoteId);
    }

    private TheOne feignClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger())
            .logLevel(Logger.Level.NONE) // modify here for debugging
            .target(TheOne.class, "https://the-one-api.dev/v2");

    private String token;

    /**
     * Default constructor, read api token from environment variable
     */
    public TheOneClient() {
        this(System.getenv(Constants.THE_ONE_TOKEN));
    }

    /**
     * Constructor to set token explicitly
     * @param token the token provided by The One API when sign up
     */
    public TheOneClient(String token) {
        this.token = token;
        if (token == null || token.isEmpty()) throw new TheOneClientException(Constants.TOKEN_REQUIRED, null);
    }


    /**
     * List all movies
     * @return Response contains pagination metadata and a list of movies
     */
    public Response<Movie> getMovies() {
        return getMovies(new Options.Builder().build());
    }

    /**
     * List all movies with custom pagination, sort and filter settings
     * @param options settings for pagination, sort and filter
     * @return Response contains pagination metadata and a list of movies
     */
    public Response<Movie> getMovies(Options options) {
        Response<Movie> res = feignClient.movies(token, options.getQueryMap(), options.getFilter());
        return res;
    }

    /**
     * Get a movie by id
     * @param movieId unique movie id
     * @return a single Movie matching the specified id
     */
    public Movie getMovieById(String movieId) {
        Response<Movie> res = feignClient.movie(token, movieId);
        if (res.getTotal() == 0) throw new TheOneClientException(Constants.NOT_FOUND, null);
        return res.getDocs().get(0);
    }

    /**
     * Get quotes in the specified movie
     * @param movieId unique movie id
     * @return Response contains pagination metadata and a list of quotes
     */
    public Response<Quote> getQuotesByMovieId(String movieId) {
        return getQuotesByMovieId(movieId, new Options.Builder().build());
    }

    /**
     * Get quotes in the specified movie with custom pagination, sort and filter settings
     * @param movieId unique movie id
     * @param options settings for pagination, sort and filter
     * @return Response contains pagination metadata and a list of quotes
     */
    public Response<Quote> getQuotesByMovieId(String movieId, Options options) {
        Response<Quote> res = feignClient.quotesByMovie(token, movieId, options.getQueryMap(), options.getFilter());
        return res;
    }

    /**
     * Get all quotes
     * @return Response contains pagination metadata and a list of quotes
     */
    public Response<Quote> getQuotes() {
        return getQuotes(new Options.Builder().build());
    }

    /**
     * Get all quotes with custom pagination, sort and filter settings
     * @param options settings for pagination, sort and filter
     * @return Response contains pagination metadata and a list of quotes
     */
    public Response<Quote> getQuotes(Options options) {
        Response<Quote> res = feignClient.quotes(token, options.getQueryMap(), options.getFilter());
        return res;
    }

    /**
     * Get a quote by quote id
     * @param quoteId unique quote id
     * @return A single quote matching the specified id
     */
    public Quote getQuoteById(String quoteId) {
        Response<Quote> res = feignClient.quote(token, quoteId);
        if (res.getTotal() == 0) throw new TheOneClientException(Constants.NOT_FOUND, null);
        return res.getDocs().get(0);
    }
}
