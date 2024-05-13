package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {

    private Dao<MovieEntity, Long> dao;

    public MovieRepository(Dao<MovieEntity, Long> dao) {
        this.dao = dao;
    }

    // Methode zum Abrufen aller Filme aus der Datenbank
    public List<MovieEntity> getAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    // Methode zum Entfernen aller Filme aus der Datenbank
    public int removeAll() throws SQLException {
        DeleteBuilder<MovieEntity, Long> deleteBuilder = dao.deleteBuilder();
        return dao.delete(deleteBuilder.prepare());
    }

    // Methode zum Abrufen eines bestimmten Films anhand seiner ID
    public MovieEntity getMovie(Long id) throws SQLException {
        return dao.queryForId(id);
    }

    // Methode zum Hinzufügen einer Liste von Filmen zur Datenbank
    public int addAllMovies(List<MovieEntity> movies) throws SQLException {
        return dao.create(movies);
    }
}
