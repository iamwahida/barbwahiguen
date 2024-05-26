package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.controllers.HomeController;
import at.ac.fhcampuswien.fhmdb.controllers.WatchlistController;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Button detailBtn = new Button("Show Details");
    private final Button actionBtn = new Button();
    private final HBox buttonBox = new HBox(10, detailBtn, actionBtn);
    private final VBox layout = new VBox(title, detail, genre, buttonBox);
    private Object controller;
    private boolean collapsedDetails = true;  // Track whether details are shown

    public MovieCell(Object controller) {
        super();
        this.controller = controller;

        configureLayout();
        configureButtons();

        setActionButtonAction();
    }

    private void configureLayout() {
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic");
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        title.fontProperty().set(title.getFont().font(20));
        detail.setWrapText(true);
        layout.setPadding(new Insets(10));
        layout.spacingProperty().set(10);
        layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
    }

    private void configureButtons() {
        detailBtn.setStyle("-fx-background-color: #f5c518;");
        actionBtn.setStyle("-fx-background-color: #f5c518;");
        detailBtn.setOnMouseClicked(mouseEvent -> toggleDetails());
    }

    private void toggleDetails() {
        if (collapsedDetails) {
            layout.getChildren().add(getDetails());
            collapsedDetails = false;
            detailBtn.setText("Hide Details");
        } else {
            layout.getChildren().remove(4);
            collapsedDetails = true;
            detailBtn.setText("Show Details");
        }
        setGraphic(layout);
    }

    private VBox getDetails() {
        VBox details = new VBox();
        details.setSpacing(5);

        Label releaseYear = new Label("Release Year: " + getItem().getReleaseYear());
        Label length = new Label("Length: " + getItem().getLengthInMinutes() + " minutes");
        Label rating = new Label("Rating: " + getItem().getRating() + "/10");
        Label directors = new Label("Directors: " + String.join(", ", getItem().getDirectors()));
        Label writers = new Label("Writers: " + String.join(", ", getItem().getWriters()));
        Label mainCast = new Label("Main Cast: " + String.join(", ", getItem().getMainCast()));

        releaseYear.getStyleClass().add("text-white");
        length.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");
        directors.getStyleClass().add("text-white");
        writers.getStyleClass().add("text-white");
        mainCast.getStyleClass().add("text-white");

        details.getChildren().addAll(releaseYear, length, rating, directors, writers, mainCast);
        return details;
    }

    public void setActionButtonAction() {
        actionBtn.setOnAction(event -> {
            Movie movie = getItem();
            if (controller != null && movie != null) {
                if (controller instanceof HomeController) {
                    try {
                        ((HomeController) controller).addMovieToWatchlist(movie);
                    } catch (DatabaseException e) {
                    }
                } else if (controller instanceof WatchlistController) {
                    ((WatchlistController) controller).removeMovieFromWatchlist(movie);
                }
            }
        });
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);
        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            title.setText(movie.getTitle());
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");
            genre.setText(movie.getGenres().stream().map(Enum::toString).collect(Collectors.joining(", ")));
            if (controller instanceof HomeController) {
                actionBtn.setText("Add");
            } else if (controller instanceof WatchlistController) {
                actionBtn.setText("Remove");
            }
            setGraphic(layout);
        }
    }
}
