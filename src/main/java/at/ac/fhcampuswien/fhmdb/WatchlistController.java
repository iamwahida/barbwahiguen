package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Movie;
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
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WatchlistController implements Initializable {

    @FXML
    public Button watchlistButton;
    @FXML
    public Button homeButton;

    @FXML
    private JFXListView<Movie> watchlistListView;

    private ObservableList<Movie> watchlist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadWatchlistFromAPI();
    }

    private void loadWatchlistFromAPI() {
        // Methode der API aufrufen, um die Filme der Watchlist abzurufen
        List<Movie> watchlistMovies = MovieAPI.getWatchlistMovies();

        // zurückgegebene List null oder nicht
        if (watchlistMovies != null) {
            watchlist.addAll(watchlistMovies);  // Filme zur ObservableList hinzufügen, um sie in der ListView anzuzeigen
        }

        // Items der ListView auf die Filme der Watchlist setzen
        watchlistListView.setItems(watchlist);
    }

    @FXML
    private void switchToWatchlist(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist-view.fxml"));
        Parent watchlistParent = loader.load();
        Scene watchlistScene = new Scene(watchlistParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(watchlistScene);
        window.show();
    }

    @FXML
    private void switchToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent homeParent  = loader.load();
        Scene homeScene  = new Scene(homeParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

}
