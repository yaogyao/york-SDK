package io.github.yaogyao.yorksdk;


import io.github.yaogyao.yorksdk.model.Movie;
import io.github.yaogyao.yorksdk.model.Quote;
import io.github.yaogyao.yorksdk.model.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class TheOneClientTest {
    private static TheOneClient client = new TheOneClient();

    @Test
    public void testListMovies(){
        Response<Movie> res = client.getMovies();
        assertTrue(res.getDocs().size() >= 0);
        assertTrue(res.getOffset() == 0);
    }

    @Test
    public void testListMoviesWithCustomLimit(){
        int limit = 2;
        assumeTrue(client.getMovies().getDocs().size() >= limit);
        Options options = new Options.Builder().limit(limit).build();
        Response<Movie> res = client.getMovies(options);
        assertTrue(res.getDocs().size() == limit);
    }
    @Test
    public void testListMoviesWithInvalidLimit(){
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Options.Builder().limit(-1).build());
        assertEquals(Constants.INVALID_LIMIT, exception.getMessage());
    }
    @Test
    public void testListMoviesWithCustomOffset(){
        Response<Movie> res1 = client.getMovies();
        int offset = 1;
        assumeTrue(res1.getDocs().size() > offset);

        Options options = new Options.Builder().offset(offset).build();
        Response<Movie> res2 = client.getMovies(options);
        assertTrue(res1.getDocs().get(offset).getId().equals(res2.getDocs().get(0).getId()));
    }
    @Test
    public void testListMoviesWithInvalidOffset(){
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Options.Builder().offset(-1).build());
        assertEquals(Constants.INVALID_OFFSET, exception.getMessage());
    }
    @Test
    public void testListMoviesWithCustomPage(){
        Response<Movie> res1 = client.getMovies();
        int limit = 5, page = 2;
        assumeTrue(res1.getDocs().size() > limit*(page-1));

        Options options = new Options.Builder().limit(limit).page(page).build();
        Response<Movie> res2 = client.getMovies(options);
        assertTrue(res1.getDocs().get(limit*(page-1)).getId().equals(res2.getDocs().get(0).getId()));
    }

    @Test
    public void testListMoviesWithInvalidPage(){
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Options.Builder().page(0).build());
        assertEquals(Constants.INVALID_PAGE, exception.getMessage());
    }

    @Test
    public void testListMoviesWithSort(){
        Response<Movie> res1 = client.getMovies();
        assumeTrue(res1.getDocs().size() >= 2);

        Options options = new Options.Builder().sort("runtimeInMinutes").build();
        Response<Movie> res2 = client.getMovies(options);
        for (int i=0; i<res2.getDocs().size()-1; i++) {
            assertTrue(res2.getDocs().get(i).getRuntimeInMinutes() <= res2.getDocs().get(i + 1).getRuntimeInMinutes());
        }
    }

    @Test
    public void testListMoviesWithInvalidSort(){
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Options.Builder().sort("").build());
        assertEquals(Constants.INVALID_SORT_NAME, exception.getMessage());
    }

    @Test
    public void testListMoviesWithMatchFilter(){
        Response<Movie> res = client.getMovies();
        int total = res.getTotal();
        assumeTrue(total >= 2);
        Movie m1 = res.getDocs().get(0);

        // match
        Options options = new Options.Builder().filter("name=" + m1.getName()).build();
        res = client.getMovies(options);
        int matchCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m1.getName().equals(m.getName())));

        // negate match
        options = new Options.Builder().filter("name!=" + m1.getName()).build();
        res = client.getMovies(options);
        int negateMatchCount = res.getTotal();
        res.getDocs().forEach(m -> assertFalse(m1.getName().equals(m.getName())));
        assertEquals(total, matchCount + negateMatchCount);
    }

    @Test
    public void testListMoviesWithIncludeFilter(){
        Response<Movie> res = client.getMovies();
        int total = res.getTotal();
        assumeTrue(total >= 2);
        Movie m1 = res.getDocs().get(0);
        Movie m2 = res.getDocs().get(1);

        // include
        Options options = new Options.Builder().filter("name=" + m1.getName() + "," + m2.getName()).build();
        res = client.getMovies(options);
        int includeCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m1.getName().equals(m.getName()) || m2.getName().equals(m.getName())));

        // exclude
        options = new Options.Builder().filter("name!=" + m1.getName() + "," + m2.getName()).build();
        res = client.getMovies(options);
        int excludeCount = res.getTotal();
        res.getDocs().forEach(m -> assertFalse(m1.getName().equals(m.getName()) || m2.getName().equals(m.getName())));
        assertEquals(total, includeCount+excludeCount);
    }

    @Test
    public void testListMoviesWithExistFilter(){
        Response<Movie> res = client.getMovies();
        int total = res.getTotal();

        // exist
        Options options = new Options.Builder().filter("name").build();
        res = client.getMovies(options);
        int existCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m.getName() != null));

        // not exist
        options = new Options.Builder().filter("!name").build();
        res = client.getMovies(options);
        int notExistCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m.getName() == null));
        assertEquals(total, existCount + notExistCount);
    }

    @Test
    public void testListMoviesWithRegexFilter(){
        Response<Movie> res = client.getMovies();
        int total = res.getTotal();

        // regex
        Options options = new Options.Builder().filter("name=/ring/i").build();
        res = client.getMovies(options);
        int regexMatchCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m.getName().toLowerCase().indexOf("ring") >= 0));

        options = new Options.Builder().filter("name!=/ring/i").build();
        res = client.getMovies(options);
        int regexNonMatchCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m.getName().toLowerCase().indexOf("ring") < 0));
        assertEquals(total, regexNonMatchCount + regexMatchCount);
    }

    @Test
    public void testListMoviesWithComparisonFilter(){
        Response<Movie> res = client.getMovies();
        int total = res.getTotal();

        // comparison
        int runtime = 200;
        Options options = new Options.Builder().filter("runtimeInMinutes>=" + runtime).build();
        res = client.getMovies(options);
        int largerOrEqualsCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m.getRuntimeInMinutes() >= runtime));

        options = new Options.Builder().filter("runtimeInMinutes<" + runtime).build();
        res = client.getMovies(options);
        int smallerCount = res.getTotal();
        res.getDocs().forEach(m -> assertTrue(m.getRuntimeInMinutes() < runtime));
        assertEquals(total, largerOrEqualsCount + smallerCount);
    }

    @Test
    public void testGetMovieById(){
        Response<Movie> res = client.getMovies();
        assumeTrue(res.getTotal() > 0);
        Movie m1 = res.getDocs().get(0);

        Movie m2 = client.getMovieById(m1.getId());
        assertEquals(m1.getId(), m2.getId());
    }

    @Test
    public void testGetMovieByInvalidId(){
        assertThrows(Exception.class, ()-> client.getMovieById("bad"));
    }

    @Test
    public void testGetQuoteByMovieId() {
        Response<Movie> res = client.getMovies();
        assumeTrue(res.getTotal() > 0);
        Movie m1 = res.getDocs().get(new Random().nextInt(res.getTotal()));

        Response<Quote> quoteResponse = client.getQuotesByMovieId(m1.getId());
        quoteResponse.getDocs().forEach(q -> assertEquals(q.getMovieId(), m1.getId()));
    }

    @Test
    public void testGetQuotes(){
        Response<Quote> res = client.getQuotes();
        int total = res.getTotal();
        assertTrue(res.getDocs().size() >= 0);

        // with limit
        assumeTrue(total > 10);
        Options options = new Options.Builder().limit(10).build();
        res = client.getQuotes(options);
        assertEquals(10, res.getDocs().size());

        // with sort and filter
        Response<Movie> movieResponse = client.getMovies();
        int count = movieResponse.getDocs().size();
        Movie m = movieResponse.getDocs().get(new Random().nextInt(count));
        options = new Options.Builder()
                .limit(10)
                .sort("dialog", SortOrder.DESC)
                .filter("movie=" + m.getId())
                .build();
        res = client.getQuotes(options);
        for (int i=0; i<res.getDocs().size()-1; i++) {
            Quote q1 = res.getDocs().get(i);
            Quote q2 = res.getDocs().get(i+1);
            assertEquals(q1.getMovieId(), q2.getMovieId());
            assertTrue(q1.getDialog().compareTo(q2.getDialog()) >= 0);
        }
    }

    @Test
    public void testGetQuoteById(){
        Options options = new Options.Builder().limit(10).build();
        Response<Quote> res = client.getQuotes(options);
        int total = res.getTotal();
        assumeTrue(total > 0);

        Quote quote = res.getDocs().get(0);

        Quote quote2 = client.getQuoteById(quote.getId());
        assertEquals(quote, quote2);
    }
}
