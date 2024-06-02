package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.database.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
        try {
            watchlistRepository = new WatchlistRepository(DatabaseManager.getInstance().getWatchlistDao());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            refreshWatchlist();
        } catch (DatabaseException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void refreshWatchlist() throws DatabaseException {
        List<Movie> moviesInWatchlist = watchlistRepository.getWatchlist().stream()
                .map(WatchlistMovieEntity::getApiId)
                .map(apiId -> {
                    try {
                        return MovieAPI.getMovieById(apiId);
                    } catch (MovieApiException e) {
                        showAlert("API Error", e.getMessage());
                        return null;
                    }
                })
                .collect(Collectors.toList());

        watchlistMovies.setAll(moviesInWatchlist);
        watchlistListView.setItems(watchlistMovies);
        watchlistListView.setCellFactory(movieListView -> new MovieCell(this));
    }

    @FXML
    private void switchToWatchlist(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist-view.fxml"));
        Parent watchlistParent = loader.load();
        Scene watchlistScene = new Scene(watchlistParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(watchlistScene);
        window.setWidth(890);  // Set the fixed width
        window.setHeight(620); // Set the fixed height
        window.show();
    }

    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml"));
        Parent homeParent = loader.load();
        Scene homeScene = new Scene(homeParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.setWidth(890);
        window.setHeight(620);
        window.show();
    }

    public void removeMovieFromWatchlist(Movie movie) {
        try {
            WatchlistMovieEntity entity = watchlistRepository.findByApiId(movie.getApiId());
            if (entity != null) {
                watchlistRepository.removeFromWatchlist(entity);
                refreshWatchlist();
            }
        } catch (DatabaseException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
