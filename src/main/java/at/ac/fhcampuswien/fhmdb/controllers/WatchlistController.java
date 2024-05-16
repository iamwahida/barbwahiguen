package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WatchlistController implements Initializable {

    @FXML
    public ListView<Movie> watchlistListView;
    @FXML
    public Button homeButton;
    @FXML
    public Button watchlistButton;

    private ObservableList<Movie> watchlistMovies = FXCollections.observableArrayList();
    private WatchlistRepository watchlistRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        watchlistRepository = new WatchlistRepository(DatabaseManager.getInstance().getWatchlistDao());
        refreshWatchlist();
    }

    private void refreshWatchlist() {
        List<Movie> moviesInWatchlist = watchlistRepository.getWatchlist().stream()
                .map(WatchlistMovieEntity::getApiId)
                .map(MovieAPI::getMovieById)
                .collect(Collectors.toList());

        watchlistMovies.setAll(moviesInWatchlist);
        watchlistListView.setItems(watchlistMovies);
        watchlistListView.setCellFactory(movieListView -> new MovieCell(this)); // Pass WatchlistController
    }

    @FXML
    private void switchToWatchlist(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist-view.fxml"));
        Parent watchlistParent = loader.load();
        Scene watchlistScene = new Scene(watchlistParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(watchlistScene);
        window.show();
    }

    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml"));
        Parent homeParent = loader.load();
        Scene homeScene = new Scene(homeParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

    public void removeMovieFromWatchlist(Movie movie) {
        WatchlistMovieEntity entity = watchlistRepository.findByApiId(movie.getApiId());
        if (entity != null) {
            watchlistRepository.removeFromWatchlist(entity);
            refreshWatchlist();
        }
    }
}
