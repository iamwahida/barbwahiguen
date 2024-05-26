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

    public MovieEntity(String apiId, String title, String description, String genres, int releaseYear, String imgUrl, int lengthInMinutes, double rating){
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    //SETTERS & GETTERS
    public String getApiId() {return apiId;}
    public void setApiId(String apiId) {this.apiId = apiId;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getGenres() {return genres;}
    public void setGenres(String genres) {this.genres = genres;}
    public int getReleaseYear() {return releaseYear;}
    public void setReleaseYear(int releaseYear) {this.releaseYear = releaseYear;}
    public String getImgUrl() {return imgUrl;}
    public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}
    public int getLengthInMinutes() {return lengthInMinutes;}
    public void setLengthInMinutes(int lengthInMinutes) {this.lengthInMinutes = lengthInMinutes;}
    public double getRating() {return rating;}
    public void setRating(double rating) {this.rating = rating;}

    //FUNCTIONS
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

    public static MovieEntity convertToMovieEntity(Movie movie) {
        return new MovieEntity(
                movie.getApiId(),
                movie.getTitle(),
                movie.getDescription(),
                convertGenresToString(movie.getGenres()),
                movie.getReleaseYear(),
                movie.getImgUrl(),
                movie.getLengthInMinutes(),
                movie.getRating()
        );
    }

    public static String convertGenresToString(List <Genre> genres){
        return genres.stream().map(Enum::name).collect(Collectors.joining(", "));
    }

    public static Movie convertToMovie(MovieEntity movieEntity){
        return new Movie(
                movieEntity.getApiId(),
                movieEntity.getTitle(),
                movieEntity.getDescription(),
                convertStringToGenre(movieEntity.getGenres()),
                movieEntity.getReleaseYear(),
                movieEntity.getImgUrl(),
                movieEntity.getLengthInMinutes(),
                movieEntity.getRating()
        );
    }

    public static List <Genre> convertStringToGenre(String genres){
        try {
            return Arrays.stream(genres.split(",\\s*")).map(Genre::valueOf).collect(Collectors.toList());
        } catch (IllegalArgumentException | NullPointerException e){
            System.err.println("Error converting string to genre: " + e.getMessage());
            return new ArrayList<>(); //Return an emtpy list
        }
    }
}
