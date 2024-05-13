package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
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
import java.util.*;

public class WatchlistController implements Initializable {

    @FXML
    public Button watchlistButton;
    @FXML
    public Button homeButton;

    @FXML
    private JFXListView<Movie> watchlistListView;

    private WatchlistRepository watchlistRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        watchlistRepository = new WatchlistRepository();

       /* MUSS NOCH GEMACHT WERDEN - wahida
        List<WatchlistRepository> watchlistMovies = watchlistRepository.getWatchlist();

        ObservableList<WatchlistRepository> watchlist = FXCollections.observableArrayList(watchlistMovies);

        watchlistListView.setItems(watchlist);
        watchlistListView.setCellFactory(param -> new ListCell<WatchlistRepository>() {
            @Override
            protected void updateItem(MovieEntity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });

        */
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
