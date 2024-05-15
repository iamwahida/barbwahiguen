package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
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
import java.sql.SQLException;
import java.util.*;

public class WatchlistController implements Initializable {

    @FXML
    public Button watchlistButton;
    @FXML
    public Button homeButton;

    @FXML
    private JFXListView<Movie> watchlistListView;

    private WatchlistRepository watchlistRepository;
    private MovieRepository movieRepository;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      //  loadWatchlist();
    }

    public WatchlistController () {

    }

    public WatchlistController(WatchlistRepository watchlistRepository, MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.movieRepository = movieRepository;
    }

    public void addMovieToWatchlist(String apiId) {
        try {
            watchlistRepository.addMovieToWatchlist(apiId);
            System.out.println("Film erfolgreich zur Watchlist hinzugefügt.");
        } catch (SQLException e) {
            // Fehlerbehandlung
            e.printStackTrace();
        }
    }

    public List<MovieEntity> getWatchlistMovies() {
        //leere List, um die Filme in die Watchlist hinzuzufügen
        List<MovieEntity> watchlistMovies = new ArrayList<>();
        try {
            //alle Objekte aus WatchlistMovieEntity abrufen
            List<WatchlistMovieEntity> watchlistEntities = watchlistRepository.getAllWatchlistMovies();
            //für jedes WatchlistMovieEntity die zugehörige MovieEntity abrufen
            for (WatchlistMovieEntity entity : watchlistEntities) {
                //Api ID des Films von WatchlistMoveEntity abrufen
                MovieEntity movie = movieRepository.getMovieByApiId(entity.getApiId());
                if (movie != null) {
                    watchlistMovies.add(movie);
                }
            }
        } catch (SQLException e) {
            // Fehlerbehandlung
            e.printStackTrace();
        }
        return watchlistMovies;
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
