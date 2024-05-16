package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    public WatchlistRepository() {}

    public boolean addToWatchList(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("api_id", apiId);
            List<WatchlistMovieEntity> existingMovie = queryBuilder.query();

            if (existingMovie.isEmpty()) {
                WatchlistMovieEntity movieEntity = new WatchlistMovieEntity();
                movieEntity.setApiId(apiId);
                dao.create(movieEntity);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding movie to watchlist", e);
        }
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving watchlist", e);
        }
    }

    public void removeFromWatchlist(WatchlistMovieEntity entity) throws DatabaseException {
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            throw new DatabaseException("Error removing movie from watchlist", e);
        }
    }

    public WatchlistMovieEntity findByApiId(String apiId) throws DatabaseException {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("api_id", apiId);
            List<WatchlistMovieEntity> movieEntities = queryBuilder.query();
            return movieEntities.isEmpty() ? null : movieEntities.get(0);
        } catch (SQLException e) {
            throw new DatabaseException("Error finding movie by API ID", e);
        }
    }
}
