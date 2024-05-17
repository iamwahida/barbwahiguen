package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//In this class we establish the DB Table that saves movie brought in through the remote API

@DatabaseTable(tableName = "movie_entity")
public class MovieEntity {
    @DatabaseField(columnName = "id", generatedId = true)
    private long id;
    @DatabaseField(columnName = "api_id")
    private String apiId;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(columnName = "genres")
    private String genres;
    @DatabaseField(columnName = "release_year")
    private int releaseYear;
    @DatabaseField(columnName = "img_url")
    private String imgUrl;
    @DatabaseField(columnName = "length_in_minutes")
    private int lengthInMinutes;
    @DatabaseField(columnName = "rating")
    private double rating;

    //A default constructor is needed so that the object can be returned from a DB query or so that a table can be created
    public MovieEntity(){};

    public static List <MovieEntity> fromMovies(List <Movie> movies){
        List <MovieEntity> listMovieEntities = new ArrayList<>();
        for(Movie movie : movies){
            listMovieEntities.add(convertToMovieEntity(movie));
        }
        return listMovieEntities;
    }

    public static List <Movie> toMovies(List <MovieEntity> movieEntities){
        List <Movie> listMovies = new ArrayList<>();
        for(MovieEntity movieEntity : movieEntities){
            listMovies.add(convertToMovie(movieEntity));
        }
        return listMovies;
    }

    public static MovieEntity convertToMovieEntity(Movie movie){
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.apiId = movie.getApiId();
        movieEntity.title = movie.getTitle();
        movieEntity.description = movie.getDescription();
        movieEntity.genres = convertGenresToString(movie.getGenres());
        movieEntity.releaseYear = movie.getReleaseYear();
        movieEntity.imgUrl = movie.getImgUrl();
        movieEntity.lengthInMinutes = movie.getLengthInMinutes();
        movieEntity.rating = movie.getRating();
        return movieEntity;
    }

    public static String convertGenresToString(List <Genre> genres){
        return genres.stream().map(Enum::name).collect(Collectors.joining(", "));
    }

    public static Movie convertToMovie(MovieEntity movieEntity){
        return new Movie(
                String.valueOf(movieEntity.apiId),
                movieEntity.title,
                movieEntity.description,
                convertStringToGenre(movieEntity.genres),
                movieEntity.releaseYear,
                movieEntity.imgUrl,
                movieEntity.lengthInMinutes,
                movieEntity.rating);
    }

    public static List <Genre> convertStringToGenre(String genres){
        return Arrays.stream(genres.split(",\\s*")).map(Genre::valueOf).collect(Collectors.toList());
    }
}
