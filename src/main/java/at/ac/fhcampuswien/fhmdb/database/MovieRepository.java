package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import java.sql.SQLException;
import java.util.List;

public class MovieRepository {

    //Create a Dao object (interface that provides methods for interacting with the database table without needing to write SQL queries)
    private Dao<MovieEntity, Long> dao;

    public MovieRepository(Dao<MovieEntity, Long> dao) {
        this.dao = dao;
    }
    public MovieRepository(){}

    //FUNCTIONS
    //Return MovieEntity by apiId
    public MovieEntity getMovieByApiId(String apiId) throws SQLException {
        // Annahme: apiId ist eindeutig für jeden Film in der Datenbank
        List<MovieEntity> movies = dao.queryForEq("api_id", apiId);
        if (!movies.isEmpty()) {
            return movies.get(0); //Es gibt nur einen Film mit einer bestimmten apiId
        } else {
            return null; // Film nicht gefunden
        }
    }

    //Return all MovieEntities
    public List <MovieEntity> getAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    //Remove all MovieEntities from the DB
    public int removeAll() throws SQLException {
        DeleteBuilder<MovieEntity, Long> deleteBuilder = dao.deleteBuilder();
        return dao.delete(deleteBuilder.prepare());
    }

    //Return one MovieEntity based on the id
    public MovieEntity getMovie(Long id) throws SQLException {
        return dao.queryForId(id);
    }

    //Add all MovieEntities to the Database
    public int addAllMovies(List <MovieEntity> movies) throws SQLException {
        try {
            return dao.create(movies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
