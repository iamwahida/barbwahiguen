package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;
    @FXML
    public TextField searchField;
    @FXML
    public JFXListView<Movie> movieListView;
    @FXML
    public JFXComboBox<String> genreComboBox;
    @FXML
    public JFXComboBox<String> releaseYearComboBox;
    @FXML
    public JFXComboBox<String> ratingFromComboBox;
    @FXML
    public JFXButton sortBtn;
    @FXML
    public JFXButton watchlistButton;
    @FXML
    public JFXButton homeButton;

    public List <Movie> allMovies;
    public List <MovieEntity> allMovieEntitiesFromDatabase;
    public List <MovieEntity> allMovieEntities;
    protected ObservableList <Movie> observableMovies = FXCollections.observableArrayList();
    protected SortedState sortedState;

    private WatchlistRepository watchlistRepository;
    private MovieRepository movieRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DatabaseManager.getInstance();
            movieRepository = new MovieRepository(DatabaseManager.getInstance().getMovieDao());
            watchlistRepository = new WatchlistRepository(DatabaseManager.getInstance().getWatchlistDao());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            initializeState();
        } catch (SQLException | DatabaseException e) {
            DatabaseManager.closeConnection();
            throw new RuntimeException(e);
        }
        initializeLayout();
    }

    public void initializeState() throws SQLException, DatabaseException {
        //Pull movies from the API into a list
        List<Movie> result = MovieAPI.getAllMovies();
        //Add the list to the MovieEntity table in the database
        allMovieEntities = MovieEntity.fromMovies(result);
        //Add all MovieEntities to the Database
        movieRepository.addAllMovies(allMovieEntities);
        //Fetch all movies from the database into a Movie List (convert from Movie Entity)
        allMovieEntitiesFromDatabase = movieRepository.getAllMovies();
        //Set allMovies List and observable List from the movies in the database/not the API
        setMovies(MovieEntity.toMovies(allMovieEntitiesFromDatabase));
        setMovieList(MovieEntity.toMovies(allMovieEntitiesFromDatabase));
        sortedState = SortedState.NONE;


        // test stream methods
        System.out.println("getMostPopularActor");
        System.out.println(getMostPopularActor(allMovies));

        System.out.println("getLongestMovieTitle");
        System.out.println(getLongestMovieTitle(allMovies));

        System.out.println("count movies from Zemeckis");
        System.out.println(countMoviesFrom(allMovies, "Robert Zemeckis"));

        System.out.println("count movies from Steven Spielberg");
        System.out.println(countMoviesFrom(allMovies, "Steven Spielberg"));

        System.out.println("getMoviesBetweenYears");
        List<Movie> between = getMoviesBetweenYears(allMovies, 1994, 2000);
        System.out.println(between.size());
        System.out.println(between.stream().map(Objects::toString).collect(Collectors.joining(", ")));
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell(this)); // apply custom cells to the listview

        // genre combobox
        genreComboBox.setItems(FXCollections.observableArrayList(
                "All", "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY", "CRIME", "DRAMA",
                "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR", "MUSICAL", "MYSTERY",
                "ROMANCE", "SCIENCE_FICTION", "SPORT", "THRILLER", "WAR", "WESTERN"
        ));
        genreComboBox.setValue("All");
        genreComboBox.setPromptText("Filter by Genre");

        // release year combobox
        ObservableList<String> years = FXCollections.observableArrayList();
        years.add("All");
        for (int year = 2024; year >= 1985; year--) {
            years.add(String.valueOf(year));
        }
        releaseYearComboBox.setItems(years);
        releaseYearComboBox.setValue("All");
        releaseYearComboBox.setPromptText("Filter by Release Year");

        // rating combobox
        ratingFromComboBox.setItems(FXCollections.observableArrayList(
                "All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        ));
        ratingFromComboBox.setValue("All");
        ratingFromComboBox.setPromptText("Filter by Rating");
    }

    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }

    // sort movies based on sortedState
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;

        return movies.stream().filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;

        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    public List<Movie> filterByReleaseYear(List<Movie> movies, String releaseYear) {
        if (releaseYear == null || releaseYear.equals("All")) return movies;

        return movies.stream().filter(movie -> Integer.toString(movie.getReleaseYear()).equals(releaseYear)).toList();
    }

    public List<Movie> filterByRating(List<Movie> movies, String ratingFrom) {
        if (ratingFrom == null || ratingFrom.equals("All")) return movies;

        return movies.stream().filter(movie -> movie.getRating() >= Double.parseDouble(ratingFrom)).toList();
    }

    public void applyAllFilters(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("All")) {
            filteredMovies = filterByGenre(filteredMovies, genre);
        }

        if (releaseYear != null && !releaseYear.equals("All")) {
            filteredMovies = filterByReleaseYear(filteredMovies, releaseYear);
        }

        if (ratingFrom != null && !ratingFrom.equals("All")) {
            filteredMovies = filterByRating(filteredMovies, ratingFrom);
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        String releaseYear = validateComboboxValue(releaseYearComboBox.getSelectionModel().getSelectedItem());
        String ratingFrom = validateComboboxValue(ratingFromComboBox.getSelectionModel().getSelectedItem());
        String genreValue = validateComboboxValue(genreComboBox.getSelectionModel().getSelectedItem());

        Genre genre = null;
        if (genreValue != null && !genreValue.equals("All")) {
            genre = Genre.valueOf(genreValue);
        }

        applyAllFilters(searchQuery, genre, releaseYear, ratingFrom);

        sortMovies(sortedState);
    }

    public String validateComboboxValue(Object value) {
        if (value != null && !value.toString().equals("All")) {
            return value.toString();
        }
        return null;
    }

    public List<Movie> getMovies(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        return MovieAPI.getAllMovies(searchQuery, genre, releaseYear, ratingFrom);
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    @FXML
    private void switchToWatchlist(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist-view.fxml"));
        Parent watchlistParent = loader.load();
        Scene watchlistScene = new Scene(watchlistParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(watchlistScene);
        window.setHeight(620);
        window.setWidth(890);
        window.show();
    }

    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml"));
        Parent homeParent = loader.load();
        Scene homeScene = new Scene(homeParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.setHeight(620);
        window.setWidth(890);
        window.show();
    }

    @FXML
    public void addMovieToWatchlist(Movie movie) throws DatabaseException {
        if (movie != null) {
            boolean success = watchlistRepository.addToWatchList(movie.getApiId());
            if (success) {
                System.out.println("Movie added to watchlist: " + movie.getTitle());
            } else {
                System.out.println("Movie already in watchlist: " + movie.getTitle());
            }
        }
    }

    // count which actor is in the most movies
    public String getMostPopularActor(List<Movie> movies) {
        String actor = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        return actor;
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
}
