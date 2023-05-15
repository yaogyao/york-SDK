# york-sdk
A Java SDK to access The One API: https://the-one-api.dev/

# Installation

## Maven

```xml
<dependency>
  <groupId>io.github.yaogyao</groupId>
  <artifactId>york-sdk</artifactId>
  <version>1.0</version>
</dependency>
```

## Gradle
```groovy
compile group: 'io.github.yaogyao', name: 'york-sdk', version: '1.0'
```

# Usage

```java
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
```

More Examples:
- src/main/java/io/github/yaogyao/yorksdk/ExampleUsage.java
- src/test/java/io/github/yaogyao/yorksdk/TheOneClientTest.java
