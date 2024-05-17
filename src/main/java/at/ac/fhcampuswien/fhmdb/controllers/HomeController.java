package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.database.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public Button searchBtn;
    @FXML
    public TextField searchField;
    @FXML
    public ListView<Movie> movieListView;
    @FXML
    public ComboBox<String> genreComboBox;
    @FXML
    public ComboBox<String> releaseYearComboBox;
    @FXML
    public ComboBox<String> ratingFromComboBox;
    @FXML
    public Button sortBtn;
    @FXML
    public Button watchlistButton;
    @FXML
    public Button homeButton;

    private List<Movie> allMovies;
    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private WatchlistRepository watchlistRepository;
    private boolean ascendingSort = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        watchlistRepository = new WatchlistRepository(DatabaseManager.getInstance().getWatchlistDao());
        allMovies = MovieAPI.getAllMovies();
        observableMovies.addAll(allMovies);
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(this));
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

    @FXML
    public void sortBtnClicked(ActionEvent event) {
        if (ascendingSort) {
            observableMovies.sort(Comparator.comparing(movie -> movie.getTitle().toLowerCase()));
            System.out.println("Movies sorted by title in ascending order");
        } else {
            observableMovies.sort(Comparator.comparing((Movie movie) -> movie.getTitle().toLowerCase()).reversed());
            System.out.println("Movies sorted by title in descending order");
        }
        ascendingSort = !ascendingSort;
    }

    @FXML
    public void searchBtnClicked(ActionEvent event) {
        String query = searchField.getText();
        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        observableMovies.setAll(filteredMovies);
    }
}
