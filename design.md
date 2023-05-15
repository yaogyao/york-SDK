# Data Model
The One API is read only.
Request is encapsulated in the URL and headers. There is no request body for these `GET` calls.
Response is following a generic JSON data format for all the calls.
```json
{
    "docs": [
        {
            "_id": "5cd95395de30eff6ebccde56",
            "key": "value"
        },
        {
            "_id": "id",
            ... 
        },
        ...
    ],
    "total": 8,
    "limit": 1000,
    "offset": 0,
    "page": 1,
    "pages": 1
}
```
A generic `Response` class is defined to deserialize the response body.
The `docs` array in the response body is dynamically defined for differently endpoints. 
It could be Book, Movie, Quote, Character, etc. The only common property is `_id` field.
A base class `Item` is defined with this `_id` field, and extended by Movie, Quote, etc.
The `Response` class is parameterized with `Item` subclasses such as `Movie` and `Quote`.

Gson is used for deserialization. 

# Request Building with Feign
Feign library (https://github.com/OpenFeign/feign) is used to construct the HTTP client. 
It handles the http method, path variables, query parameters and headers easily. It also handles
all the lower level plumbing.

A `Options` class is defined to capture pagination, sort and filter settings. It contains a fluent-style `Builder`. 
The options can be passed (optionally) to `TheOneClient` when making plural queries.

To access The One API, user need to sign up (https://the-one-api.dev/sign-up) and get a api token. 
The token can be set in the constructor explicitly, or set in the environment variable `THE_ONE_TOKEN`. 
The token is added to the request `Authorization` header for every call.

# SDK Interfaces
This SDK provides these interfaces:
- Response<Movie> getMovies()
- Response<Movie> getMovies(options)
- Movie getMovieById(movieId)
- Response<Quote> getQuotesByMovieId(movieId, options)
- Response<Quote> getQuotes()
- Response<Quote> getQuotes(options)
- Quote getQuoteById(quoteId)

All REST endpoints return generic `Response<T>` which containing `docs` array and pagination information. 
This information is useful for subsequent pagination calls, but for endpoints returning a singular item, 
these pagination information is meaningless, thus the SDK interface return the item directly instead of generic `Response`.

For similar reason, there is no pagination, sort and filter options for singular request call.

# Testing
The Feign library does the heavy lifting for constructing request and parsing response. 
Instead of unit testing on the URL request building and response parsing, I focused on integration test with
the live API endpoints. See examples in TheOneClientTest.java.

# Logging and Debugging
set log level to inspect request and response.
```java
    private TheOne feignClient = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger())
            .logLevel(Logger.Level.FULL) // modify here for debugging
            .target(TheOne.class, "https://the-one-api.dev/v2");
```

# Distribution
Use `mvn clean deploy -Prelease` to push the artifacts to maven repositories.
TODO: setup CI/CD in github with github actions

# Future Extension
- support additional return type such as `Character`, `Chapter` by extending `Item`
- support additional endpoints by adding corresponding methods in `TheOneClient$TheOne` interface, as well as `TheOneClient` class
