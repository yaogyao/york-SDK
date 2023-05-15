package io.github.yaogyao.yorksdk.model;

import java.util.Objects;

public class Movie extends Item {
    private String name;
    private int runtimeInMinutes;
    private double budgetInMillions;
    private double boxOfficeRevenueInMillions;
    private int academyAwardNominations;
    private int academyAwardWins;
    private double rottenTomatoesScore;

    public String getName() {
        return name;
    }

    public int getRuntimeInMinutes() {
        return runtimeInMinutes;
    }

    public double getBudgetInMillions() {
        return budgetInMillions;
    }

    public double getBoxOfficeRevenueInMillions() {
        return boxOfficeRevenueInMillions;
    }

    public int getAcademyAwardNominations() {
        return academyAwardNominations;
    }

    public int getAcademyAwardWins() {
        return academyAwardWins;
    }

    public double getRottenTomatoesScore() {
        return rottenTomatoesScore;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", runtimeInMinutes=" + runtimeInMinutes +
                ", budgetInMillions=" + budgetInMillions +
                ", boxOfficeRevenueInMillions=" + boxOfficeRevenueInMillions +
                ", academyAwardNominations=" + academyAwardNominations +
                ", academyAwardWins=" + academyAwardWins +
                ", rottenTomatoesScore=" + rottenTomatoesScore +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return runtimeInMinutes == movie.runtimeInMinutes && Double.compare(movie.budgetInMillions, budgetInMillions) == 0 && Double.compare(movie.boxOfficeRevenueInMillions, boxOfficeRevenueInMillions) == 0 && academyAwardNominations == movie.academyAwardNominations && academyAwardWins == movie.academyAwardWins && Double.compare(movie.rottenTomatoesScore, rottenTomatoesScore) == 0 && Objects.equals(name, movie.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, runtimeInMinutes, budgetInMillions, boxOfficeRevenueInMillions, academyAwardNominations, academyAwardWins, rottenTomatoesScore);
    }
}
