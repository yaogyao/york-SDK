package io.github.yaogyao.yorksdk;

import io.github.yaogyao.yorksdk.model.Quote;
import io.github.yaogyao.yorksdk.model.Response;
import io.github.yaogyao.yorksdk.model.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example for using TheOneClient
 *
 */
public class ExampleUsage
{
    private static Logger log = LoggerFactory.getLogger(ExampleUsage.class);

    public static void main( String[] args )
    {
        // create the client (set token in ENV variable "THE_ONE_TOKEN")
        TheOneClient client = new TheOneClient();

        // list all movies
        Response<Movie> res = client.getMovies();
        log.info("Found {} movies, default limit: {}", res.getTotal(), res.getLimit());

        // list movies with pagination, sort
        Options options = new Options.Builder()
                .limit(10)
                .page(2)
                .sort("runtimeInMinutes", SortOrder.DESC)
                .build();
        res = client.getMovies(options);
        log.info("Found movies with pagination and sort");
        res.getDocs().forEach(movie -> log.info(movie.toString()));

        // get movie by id
        Movie m = client.getMovieById("5cd95395de30eff6ebccde5d");
        log.info("Found movie with id={}: {}", m.getId(), m.toString());

        // get quotes by movie
        Response<Quote> quoteResponse = client.getQuotesByMovieId(m.getId());
        log.info("Found {} quotes for movie id={}", quoteResponse.getTotal(), m.getId());

        // get quotes
        options = new Options.Builder()
                .limit(10)
                .offset(5)
                .filter("dialog=/ring/i")
                .build();
        quoteResponse = client.getQuotes(options);
        log.info("Found {} quotes containing 'ring' case insensitive", quoteResponse.getTotal());

        // get quote by id
        Quote quote = client.getQuoteById("5cd96e05de30eff6ebcce810");
        log.info("Found quote by id={} : {}", quote.getId(), quote.toString());

    }
}
