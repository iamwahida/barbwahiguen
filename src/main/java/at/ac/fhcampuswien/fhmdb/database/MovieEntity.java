package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.ArrayList;
import java.util.List;

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

    String genresToString(List<Genre> genres){
        return this.genres;
    }

    List <MovieEntity> fromMovies(List <Movie> movies){
        List <MovieEntity> listMovieEntities = new ArrayList<>();
        return listMovieEntities;
    }

    List <Movie> toMovies(List <MovieEntity> movies){
        List <Movie> listMovies = new ArrayList<>();
        return listMovies;
    }
}
