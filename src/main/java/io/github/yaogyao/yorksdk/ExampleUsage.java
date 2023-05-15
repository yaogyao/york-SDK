package io.github.yaogyao.yorksdk;

import io.github.yaogyao.yorksdk.model.Quote;
import io.github.yaogyao.yorksdk.model.Response;
import io.github.yaogyao.yorksdk.model.Movie;

/**
 * Example for using TheOneClient
 *
 */
public class ExampleUsage
{
    public static void main( String[] args )
    {
        // create the client
        // requires Authorization token in ENV variable "THE_ONE_TOKEN", or set explicitly in constructor
        TheOneClient client = new TheOneClient();

        // list all movies
        // Reponse<T> contains metadata on the result set, useful for subsequent pagination calls
        Response<Movie> res = client.getMovies();
        System.out.println("Found " + res.getTotal() + " movies with default limit " + res.getLimit());

        // list movies with options
        // Options contains pagination, sort and filter settings
        Options options = new Options.Builder()
                .limit(5)
                .page(2)
                .sort("runtimeInMinutes", SortOrder.DESC)
                .build();
        res = client.getMovies(options);
        System.out.println("Found " + res.getDocs().size() + " movies with page 2 and sorted by 'runtimeInMinutes' in descending order");
        res.getDocs().forEach(movie -> System.out.println(movie));

        // get movie by id
        Movie m = client.getMovieById("5cd95395de30eff6ebccde5d");
        System.out.println("Found movie with id=" + m.getId() + " - " + m);

        // get quotes by movie
        Response<Quote> quoteResponse = client.getQuotesByMovieId(m.getId());
        System.out.println("Found " + quoteResponse.getTotal() + " quotes for movie id=" + m.getId());

        // get quotes
        options = new Options.Builder()
                .limit(10)
                .offset(5)
                .filter("dialog=/ring/i")
                .build();
        quoteResponse = client.getQuotes(options);
        System.out.println("Found " + quoteResponse.getTotal() + " quotes containing 'ring' case insensitive");

        // get quote by id
        Quote quote = client.getQuoteById("5cd96e05de30eff6ebcce810");
        System.out.println("Found quote by id=" + quote.getId() + " - " + quote);
    }
}
