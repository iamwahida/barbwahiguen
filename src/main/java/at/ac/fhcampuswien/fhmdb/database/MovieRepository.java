package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import java.sql.SQLException;
import java.util.List;

public class MovieRepository {

    // Create a Dao object (interface that provides methods for interacting with the database table without needing to write SQL queries)
    private Dao<MovieEntity, Long> dao;

    public MovieRepository(Dao<MovieEntity, Long> dao) {
        if(dao == null){
            throw new IllegalArgumentException("DAO cannot be null");
        }
        this.dao = dao;
    }

    public MovieRepository() {}

    // FUNCTIONS
    // Return MovieEntity by apiId
    public MovieEntity getMovieByApiId(String apiId) throws DatabaseException {
        try {
            List<MovieEntity> movies = dao.queryForEq("api_id", apiId); //Annahme: apiId ist eindeutig für Jeden Film in der DB
            if (!movies.isEmpty()) {
                return movies.get(0); // Es gibt nur einen Film mit einer bestimmten apiId
            } else {
                return null; // Film nicht gefunden
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving movie by API ID: " + apiId, e);
        }
    }

    // Return all MovieEntities
    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all movies", e);
        }
    }

    // Remove all MovieEntities from the DB
    public int removeAll() throws DatabaseException {
        try {
            DeleteBuilder<MovieEntity, Long> deleteBuilder = dao.deleteBuilder();
            return dao.delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            throw new DatabaseException("Error removing all movies", e);
        }
    }

    // Return one MovieEntity based on the id
    public MovieEntity getMovie(Long id) throws DatabaseException {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving movie by ID: " + id, e);
        }
    }

    // Add all MovieEntities to the Database
    public int addAllMovies(List<MovieEntity> movies) throws DatabaseException {
        try {
            return dao.create(movies);
        } catch (SQLException e) {
            throw new DatabaseException("Error adding all movies", e);
        }
    }
}
